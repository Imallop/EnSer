package gal.usc.etse.es.motorhome.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record WaypointRequest(
        @NotNull Long routeId,
        @NotBlank String name,
        @NotNull Double latitude,
        @NotNull Double longitude,
        String description) {
}
