package gal.usc.etse.es.motorhome.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PublicationRequest(
        @NotNull @NotBlank String authorUsername,
        Long routeId,
        @NotBlank String title,
        @NotBlank String content,
        byte[] photo) {
}
