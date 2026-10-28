package gal.usc.etse.es.motorhome.model.dto;

import gal.usc.etse.es.motorhome.model.entity.Motorbike;

public record MotorbikeResponse(Long id, String ownerUsername, String brand, String model, Integer year) {
    public static MotorbikeResponse from(Motorbike motorbike) {
        return new MotorbikeResponse(motorbike.getId(), motorbike.getOwner().getUsername(),
                motorbike.getBrand(), motorbike.getModel(), motorbike.getYear());
    }
}
