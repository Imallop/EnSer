package gal.usc.etse.es.motorhome.service;

import gal.usc.etse.es.motorhome.exception.ResourceNotFoundException;
import gal.usc.etse.es.motorhome.model.dto.MotorbikeRequest;
import gal.usc.etse.es.motorhome.model.entity.Motorbike;
import gal.usc.etse.es.motorhome.model.entity.User;
import gal.usc.etse.es.motorhome.repository.MotorbikeRepository;
import gal.usc.etse.es.motorhome.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MotorbikeServiceTest {
    private final MotorbikeRepository bikes = mock(MotorbikeRepository.class);
    private final UserRepository users = mock(UserRepository.class);
    private final MotorbikeService service = new MotorbikeService(bikes, users);
    private final User owner = new User("ana", "secret-hash", "Ana", "ana@example.test");
    private final MotorbikeRequest request = new MotorbikeRequest("ana", "Honda", "CB500", 2020);

    @Test
    void createsForExistingOwnerAndReturnsOnlyPublicData() {
        when(users.findById("ana")).thenReturn(Optional.of(owner));
        when(bikes.save(any())).thenAnswer(invocation -> {
            Motorbike bike = invocation.getArgument(0);
            bike.setId(1L);
            return bike;
        });
        var response = service.create(request);
        assertEquals(1L, response.id());
        assertEquals("ana", response.ownerUsername());
        assertEquals("Honda", response.brand());
    }

    @Test
    void rejectsMissingOwnerWithoutSaving() {
        when(users.findById("missing")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.create(new MotorbikeRequest("missing", "Honda", "CB500", 2020)));
        verifyNoInteractions(bikes);
    }

    @Test
    void rejectsMissingBikeOnUpdateAndDelete() {
        when(users.findById("ana")).thenReturn(Optional.of(owner));
        when(bikes.findById(8L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.update(8L, request));
        assertThrows(ResourceNotFoundException.class, () -> service.delete(8L));
        verify(bikes, never()).save(any());
        verify(bikes, never()).delete(any());
    }

    @Test
    void readsListsUpdatesAndDeletesWithoutAuthenticatedUser() {
        Motorbike bike = new Motorbike(owner, "Old", "Old", 2000);
        bike.setId(2L);
        when(users.findById("ana")).thenReturn(Optional.of(owner));
        when(bikes.findById(2L)).thenReturn(Optional.of(bike));
        when(bikes.save(bike)).thenReturn(bike);
        var page = PageRequest.of(0, 10);
        when(bikes.findAll(page)).thenReturn(new PageImpl<>(List.of(bike)));
        assertEquals(2L, service.findById(2L).id());
        assertEquals(1, service.list(page).getTotalElements());
        assertEquals("Honda", service.update(2L, request).brand());
        service.delete(2L);
        verify(bikes).delete(bike);
    }
}
