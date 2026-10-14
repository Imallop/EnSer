package gal.usc.etse.es.motorhome.repository;

import gal.usc.etse.es.motorhome.model.entity.Waypoint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WaypointRepository extends JpaRepository<Waypoint, Long> {
}
