package gal.usc.etse.es.motorhome.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// Versión sin autenticación: el cliente indica un propietario existente.
public record MotorbikeRequest(
        @NotBlank String ownerUsername,
        @NotBlank String brand,
        @NotBlank String model,
        @NotNull @Min(1885) Integer year) { }
