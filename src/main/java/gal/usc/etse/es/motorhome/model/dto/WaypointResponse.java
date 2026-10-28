package gal.usc.etse.es.motorhome.model.dto;

import gal.usc.etse.es.motorhome.model.entity.Waypoint;

public record WaypointResponse(
        Long id,
        Long routeId,
        String name,
        Double latitude,
        Double longitude,
        String description) {

    public static WaypointResponse from(Waypoint waypoint) {
        return new WaypointResponse(
                waypoint.getId(),
                waypoint.getRoute() != null ? waypoint.getRoute().getId() : null,
                waypoint.getName(),
                waypoint.getLatitude(),
                waypoint.getLongitude(),
                waypoint.getDescription());
    }
}
