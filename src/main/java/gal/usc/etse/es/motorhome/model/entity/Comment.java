package gal.usc.etse.es.motorhome.model.entity;

import lombok.*;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "comments")
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_username", nullable = false)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publication_id")
    private Publication publication;

    @Column(nullable = false, length = 3000)
    private String content;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public Comment(User author, Publication publication, String content, LocalDateTime createdAt) {
        this.author = author;
        this.publication = publication;
        this.content = content;
        this.createdAt = createdAt;
    }
}
