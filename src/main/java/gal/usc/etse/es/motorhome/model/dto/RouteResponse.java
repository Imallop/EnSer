package gal.usc.etse.es.motorhome.model.dto;

import gal.usc.etse.es.motorhome.model.entity.Route;

import java.time.LocalDateTime;

public record RouteResponse(
        Long id,
        String ownerUsername,
        String name,
        String description,
        Double distanceKm,
        LocalDateTime createdAt) {

    public static RouteResponse from(Route route) {
        return new RouteResponse(
                route.getId(),
                route.getOwner() != null ? route.getOwner().getUsername() : null,
                route.getName(),
                route.getDescription(),
                route.getDistanceKm(),
                route.getCreatedAt());
    }
}
