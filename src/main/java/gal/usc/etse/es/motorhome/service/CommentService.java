package gal.usc.etse.es.motorhome.service;

import gal.usc.etse.es.motorhome.exception.ResourceNotFoundException;
import gal.usc.etse.es.motorhome.model.dto.CommentRequest;
import gal.usc.etse.es.motorhome.model.dto.CommentResponse;
import gal.usc.etse.es.motorhome.model.entity.Comment;
import gal.usc.etse.es.motorhome.model.entity.Publication;
import gal.usc.etse.es.motorhome.model.entity.User;
import gal.usc.etse.es.motorhome.repository.CommentRepository;
import gal.usc.etse.es.motorhome.repository.PublicationRepository;
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
public class CommentService {
    private final CommentRepository comments;
    private final UserRepository users;
    private final PublicationRepository publications;

    public CommentService(CommentRepository comments, UserRepository users, PublicationRepository publications) {
        this.comments = comments;
        this.users = users;
        this.publications = publications;
    }

    @Transactional
    public CommentResponse create(@NotNull @Valid CommentRequest request) {
        User author = requireAuthor(request.authorUsername());
        Publication publication = requirePublication(request.publicationId());

        Comment comment = new Comment(
                author,
                publication,
                request.content(),
                LocalDateTime.now());

        return CommentResponse.from(comments.save(comment));
    }

    public CommentResponse findById(@NotNull Long id) {
        return CommentResponse.from(requireComment(id));
    }

    public Page<CommentResponse> list(Pageable pageable) {
        return comments.findAll(pageable).map(CommentResponse::from);
    }

    @Transactional
    public CommentResponse update(@NotNull Long id, @NotNull @Valid CommentRequest request) {
        Comment comment = requireComment(id);
        comment.setAuthor(requireAuthor(request.authorUsername()));
        comment.setPublication(requirePublication(request.publicationId()));
        comment.setContent(request.content());
        return CommentResponse.from(comments.save(comment));
    }

    @Transactional
    public void delete(@NotNull Long id) {
        comments.delete(requireComment(id));
    }

    private User requireAuthor(String username) {
        return users.findById(username)
                .orElseThrow(() -> new ResourceNotFoundException("El autor del comentario no existe"));
    }

    private Publication requirePublication(Long publicationId) {
        return publications.findById(publicationId)
                .orElseThrow(() -> new ResourceNotFoundException("La publicación del comentario no existe"));
    }

    private Comment requireComment(Long id) {
        return comments.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El comentario no existe"));
    }
}
