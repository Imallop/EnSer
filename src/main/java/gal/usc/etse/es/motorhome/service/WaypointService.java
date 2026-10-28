package gal.usc.etse.es.motorhome.service;

import gal.usc.etse.es.motorhome.exception.ResourceNotFoundException;
import gal.usc.etse.es.motorhome.model.dto.WaypointRequest;
import gal.usc.etse.es.motorhome.model.dto.WaypointResponse;
import gal.usc.etse.es.motorhome.model.entity.Route;
import gal.usc.etse.es.motorhome.model.entity.Waypoint;
import gal.usc.etse.es.motorhome.repository.RouteRepository;
import gal.usc.etse.es.motorhome.repository.WaypointRepository;
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
public class WaypointService {
    private final WaypointRepository waypoints;
    private final RouteRepository routes;

    public WaypointService(WaypointRepository waypoints, RouteRepository routes) {
        this.waypoints = waypoints;
        this.routes = routes;
    }

    @Transactional
    public WaypointResponse create(@NotNull @Valid WaypointRequest request) {
        Route route = requireRoute(request.routeId());

        Waypoint waypoint = new Waypoint(
                route,
                request.name(),
                request.latitude(),
                request.longitude(),
                request.description());

        return WaypointResponse.from(waypoints.save(waypoint));
    }

    public WaypointResponse findById(@NotNull Long id) {
        return WaypointResponse.from(requireWaypoint(id));
    }

    public Page<WaypointResponse> list(Pageable pageable) {
        return waypoints.findAll(pageable).map(WaypointResponse::from);
    }

    @Transactional
    public WaypointResponse update(@NotNull Long id, @NotNull @Valid WaypointRequest request) {
        Waypoint waypoint = requireWaypoint(id);
        waypoint.setRoute(requireRoute(request.routeId()));
        waypoint.setName(request.name());
        waypoint.setLatitude(request.latitude());
        waypoint.setLongitude(request.longitude());
        waypoint.setDescription(request.description());
        return WaypointResponse.from(waypoints.save(waypoint));
    }

    @Transactional
    public void delete(@NotNull Long id) {
        waypoints.delete(requireWaypoint(id));
    }

    private Route requireRoute(Long routeId) {
        return routes.findById(routeId)
                .orElseThrow(() -> new ResourceNotFoundException("La ruta del waypoint no existe"));
    }

    private Waypoint requireWaypoint(Long id) {
        return waypoints.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El waypoint no existe"));
    }
}
