package gal.usc.etse.es.motorhome.model.dto;

import gal.usc.etse.es.motorhome.model.entity.Publication;

import java.time.LocalDateTime;

public record PublicationResponse(
        Long id,
        String authorUsername,
        Long routeId,
        String title,
        String content,
        byte[] photo,
        LocalDateTime createdAt) {

    public static PublicationResponse from(Publication publication) {
        Long routeId = publication.getRoute() != null ? publication.getRoute().getId() : null;
        return new PublicationResponse(
                publication.getId(),
                publication.getAuthor() != null ? publication.getAuthor().getUsername() : null,
                routeId,
                publication.getTitle(),
                publication.getContent(),
                publication.getPhoto(),
                publication.getCreatedAt());
    }
}
