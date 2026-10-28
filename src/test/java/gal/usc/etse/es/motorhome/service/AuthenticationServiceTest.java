package gal.usc.etse.es.motorhome.service;

import gal.usc.etse.es.motorhome.model.dto.LoginRequest;
import gal.usc.etse.es.motorhome.model.entity.User;
import gal.usc.etse.es.motorhome.repository.RefreshTokenRepository;
import gal.usc.etse.es.motorhome.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.KeyPair;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {
    @Test
    void issuesVerifiableJwtWithStoredIdentityAndRejectsWrongCredentials() {
        AuthenticationManager manager = mock(AuthenticationManager.class);
        UserRepository users = mock(UserRepository.class);
        KeyPair keys = Jwts.SIG.RS256.keyPair().build();
        var service = new AuthenticationService(manager, keys, users, mock(RefreshTokenRepository.class));
        ReflectionTestUtils.setField(service, "tokenTTL", Duration.ofMinutes(15));
        when(manager.authenticate(any())).thenReturn(
                UsernamePasswordAuthenticationToken.authenticated("ana", null, List.of()));
        when(users.findByUsername("ana")).thenReturn(Optional.of(
                new User("ana", "hash", "Nombre real", "ana@example.test")));
        var response = service.login(new LoginRequest("ana", "password"));
        var claims = Jwts.parser().verifyWith(keys.getPublic()).build()
                .parseSignedClaims(response.token()).getPayload();
        assertEquals("ana", claims.getSubject());
        assertEquals("Nombre real", response.name());
        assertTrue(claims.getExpiration().after(claims.getIssuedAt()));
        when(manager.authenticate(any())).thenThrow(new BadCredentialsException("wrong"));
        assertThrows(BadCredentialsException.class, () -> service.login(new LoginRequest("ana", "wrong")));
    }
}
