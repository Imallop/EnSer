package gal.usc.etse.es.motorhome.model.dto;

import gal.usc.etse.es.motorhome.model.entity.Comment;

import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        String authorUsername,
        Long publicationId,
        String content,
        LocalDateTime createdAt) {

    public static CommentResponse from(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getAuthor() != null ? comment.getAuthor().getUsername() : null,
                comment.getPublication() != null ? comment.getPublication().getId() : null,
                comment.getContent(),
                comment.getCreatedAt());
    }
}
