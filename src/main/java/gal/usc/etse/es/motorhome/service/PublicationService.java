package gal.usc.etse.es.motorhome.service;

import gal.usc.etse.es.motorhome.exception.ResourceNotFoundException;
import gal.usc.etse.es.motorhome.model.dto.PublicationRequest;
import gal.usc.etse.es.motorhome.model.dto.PublicationResponse;
import gal.usc.etse.es.motorhome.model.entity.Publication;
import gal.usc.etse.es.motorhome.model.entity.Route;
import gal.usc.etse.es.motorhome.model.entity.User;
import gal.usc.etse.es.motorhome.repository.PublicationRepository;
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
public class PublicationService {
    private final PublicationRepository publications;
    private final UserRepository users;
    private final RouteRepository routes;

    public PublicationService(PublicationRepository publications, UserRepository users, RouteRepository routes) {
        this.publications = publications;
        this.users = users;
        this.routes = routes;
    }

    @Transactional
    public PublicationResponse create(@NotNull @Valid PublicationRequest request) {
        User author = requireAuthor(request.authorUsername());
        Route route = request.routeId() != null ? requireRoute(request.routeId()) : null;

        Publication publication = new Publication(
                author,
                route,
                request.title(),
                request.content(),
                request.photo(),
                LocalDateTime.now());

        return PublicationResponse.from(publications.save(publication));
    }

    public PublicationResponse findById(@NotNull Long id) {
        return PublicationResponse.from(requirePublication(id));
    }

    public Page<PublicationResponse> list(Pageable pageable) {
        return publications.findAll(pageable).map(PublicationResponse::from);
    }

    @Transactional
    public PublicationResponse update(@NotNull Long id, @NotNull @Valid PublicationRequest request) {
        Publication publication = requirePublication(id);
        publication.setAuthor(requireAuthor(request.authorUsername()));
        publication.setRoute(request.routeId() != null ? requireRoute(request.routeId()) : null);
        publication.setTitle(request.title());
        publication.setContent(request.content());
        publication.setPhoto(request.photo());
        return PublicationResponse.from(publications.save(publication));
    }

    @Transactional
    public void delete(@NotNull Long id) {
        publications.delete(requirePublication(id));
    }

    private User requireAuthor(String username) {
        return users.findById(username)
                .orElseThrow(() -> new ResourceNotFoundException("El autor no existe"));
    }

    private Route requireRoute(Long routeId) {
        return routes.findById(routeId)
                .orElseThrow(() -> new ResourceNotFoundException("La ruta no existe"));
    }

    private Publication requirePublication(Long id) {
        return publications.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("La publicación no existe"));
    }
}
