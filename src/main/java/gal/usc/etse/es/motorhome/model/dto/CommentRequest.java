package gal.usc.etse.es.motorhome.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CommentRequest(
        @NotBlank String authorUsername,
        @NotNull Long publicationId,
        @NotBlank String content) {
}
