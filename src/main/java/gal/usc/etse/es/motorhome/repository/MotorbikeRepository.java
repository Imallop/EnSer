package gal.usc.etse.es.motorhome.repository;

import gal.usc.etse.es.motorhome.model.entity.Motorbike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MotorbikeRepository extends JpaRepository<Motorbike, Long> {
    Optional<Motorbike> findByIdAndOwnerUsername(Long id, String username);
    Page<Motorbike> findByOwnerUsername(String username, Pageable pageable);
}
