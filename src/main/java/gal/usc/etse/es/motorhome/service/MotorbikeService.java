package gal.usc.etse.es.motorhome.service;

import gal.usc.etse.es.motorhome.exception.ResourceNotFoundException;
import gal.usc.etse.es.motorhome.model.dto.MotorbikeRequest;
import gal.usc.etse.es.motorhome.model.dto.MotorbikeResponse;
import gal.usc.etse.es.motorhome.model.entity.Motorbike;
import gal.usc.etse.es.motorhome.model.entity.User;
import gal.usc.etse.es.motorhome.repository.MotorbikeRepository;
import gal.usc.etse.es.motorhome.repository.UserRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@Transactional(readOnly = true)
public class MotorbikeService {
    private final MotorbikeRepository motorbikes;
    private final UserRepository users;

    public MotorbikeService(MotorbikeRepository motorbikes, UserRepository users) {
        this.motorbikes = motorbikes;
        this.users = users;
    }

    @Transactional
    public MotorbikeResponse create(@NotNull @Valid MotorbikeRequest request) {
        User owner = requireOwner(request.ownerUsername());
        return MotorbikeResponse.from(motorbikes.save(
                new Motorbike(owner, request.brand(), request.model(), request.year())));
    }

    public MotorbikeResponse findById(@NotNull Long id) {
        return MotorbikeResponse.from(requireMotorbike(id));
    }

    public Page<MotorbikeResponse> list(Pageable pageable) {
        return motorbikes.findAll(pageable).map(MotorbikeResponse::from);
    }

    @Transactional
    public MotorbikeResponse update(@NotNull Long id, @NotNull @Valid MotorbikeRequest request) {
        Motorbike motorbike = requireMotorbike(id);
        motorbike.setOwner(requireOwner(request.ownerUsername()));
        motorbike.setBrand(request.brand());
        motorbike.setModel(request.model());
        motorbike.setYear(request.year());
        return MotorbikeResponse.from(motorbikes.save(motorbike));
    }

    @Transactional
    public void delete(@NotNull Long id) {
        motorbikes.delete(requireMotorbike(id));
    }

    private User requireOwner(String username) {
        return users.findById(username)
                .orElseThrow(() -> new ResourceNotFoundException("El propietario no existe"));
    }

    private Motorbike requireMotorbike(Long id) {
        return motorbikes.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("La moto no existe"));
    }
}
