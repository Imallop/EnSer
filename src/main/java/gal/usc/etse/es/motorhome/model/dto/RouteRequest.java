package gal.usc.etse.es.motorhome.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RouteRequest(
        @NotBlank String ownerUsername,
        @NotBlank String name,
        String description,
        @NotNull Double distanceKm) {
}
