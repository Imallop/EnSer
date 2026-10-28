package gal.usc.etse.es.motorhome.service;

import gal.usc.etse.es.motorhome.exception.ResourceNotFoundException;
import gal.usc.etse.es.motorhome.model.dto.RouteRequest;
import gal.usc.etse.es.motorhome.model.dto.RouteResponse;
import gal.usc.etse.es.motorhome.model.entity.Route;
import gal.usc.etse.es.motorhome.model.entity.User;
import gal.usc.etse.es.motorhome.repository.RouteRepository;
import gal.usc.etse.es.motorhome.repository.UserRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

@Service
@Validated
@Transactional(readOnly = true)
public class RouteService {
    private final RouteRepository routes;
    private final UserRepository users;

    public RouteService(RouteRepository routes, UserRepository users) {
        this.routes = routes;
        this.users = users;
    }

    @Transactional
    public RouteResponse create(@NotNull @Valid RouteRequest request) {
        User owner = requireOwner(request.ownerUsername());

        Route route = new Route(
                owner,
                request.name(),
                request.description(),
                request.distanceKm(),
                LocalDateTime.now());

        return RouteResponse.from(routes.save(route));
    }

    public RouteResponse findById(@NotNull Long id) {
        return RouteResponse.from(requireRoute(id));
    }

    public Page<RouteResponse> list(Pageable pageable) {
        return routes.findAll(pageable).map(RouteResponse::from);
    }

    @Transactional
    public RouteResponse update(@NotNull Long id, @NotNull @Valid RouteRequest request) {
        Route route = requireRoute(id);
        route.setOwner(requireOwner(request.ownerUsername()));
        route.setName(request.name());
        route.setDescription(request.description());
        route.setDistanceKm(request.distanceKm());
        return RouteResponse.from(routes.save(route));
    }

    @Transactional
    public void delete(@NotNull Long id) {
        routes.delete(requireRoute(id));
    }

    private User requireOwner(String username) {
        return users.findById(username)
                .orElseThrow(() -> new ResourceNotFoundException("El propietario de la ruta no existe"));
    }

    private Route requireRoute(Long id) {
        return routes.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("La ruta no existe"));
    }
}
