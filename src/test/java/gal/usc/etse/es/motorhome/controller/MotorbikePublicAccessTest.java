package gal.usc.etse.es.motorhome.controller;

import gal.usc.etse.es.motorhome.configuration.SecurityConfiguration;
import gal.usc.etse.es.motorhome.filter.JWTFilter;
import gal.usc.etse.es.motorhome.model.dto.MotorbikeRequest;
import gal.usc.etse.es.motorhome.model.dto.MotorbikeResponse;
import gal.usc.etse.es.motorhome.service.AuthenticationService;
import gal.usc.etse.es.motorhome.service.MotorbikeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = MotorbikePublicAccessTest.Config.class)
class MotorbikePublicAccessTest {
    @Configuration
    @org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
    @EnableWebMvc
    @Import({SecurityConfiguration.class, JWTFilter.class, MotorbikeController.class})
    static class Config {
        @Bean
        static org.springframework.core.convert.ConversionService conversionService() {
            return new org.springframework.boot.convert.ApplicationConversionService();
        }
        @Bean
        MotorbikeService motorbikes() { return mock(MotorbikeService.class); }
        @Bean
        AuthenticationService authentication() {
            var service = mock(AuthenticationService.class);
            when(service.loadRoleHierarchy()).thenReturn(mock(RoleHierarchy.class));
            return service;
        }
    }

    @Autowired WebApplicationContext context;
    @Autowired MotorbikeService motorbikes;
    @Autowired AuthenticationService authentication;
    MockMvc mvc;

    @BeforeEach
    void setUp() {
        reset(motorbikes);
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    void createsAndReadsWithoutLoginEvenWithAnObsoleteToken() throws Exception {
        var response = new MotorbikeResponse(1L, "dev", "Honda", "CB500", 2020);
        when(motorbikes.create(new MotorbikeRequest("dev", "Honda", "CB500", 2020))).thenReturn(response);
        when(motorbikes.findById(1L)).thenReturn(response);
        mvc.perform(post("/motorbikes").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"ownerUsername":"dev","brand":"Honda","model":"CB500","year":2020}
                        """))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.ownerUsername").value("dev"));
        mvc.perform(get("/motorbikes/1").header("Authorization", "Bearer obsolete"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1));
        verify(authentication, never()).parseJWT(anyString());
    }

    @Test
    void validatesPublicRequestsAndAllowsDeletion() throws Exception {
        mvc.perform(post("/motorbikes").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"brand":"Honda","model":"CB500","year":2020}
                        """))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(motorbikes);
        mvc.perform(delete("/motorbikes/1")).andExpect(status().isNoContent());
        verify(motorbikes).delete(1L);
    }
}
