package gal.usc.etse.es.motorhome.model.entity;

<<<<<<< HEAD
import jakarta.persistence.*;
import lombok.*;

=======
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.CascadeType;
import jakarta.validation.constraints.AssertTrue;

import java.time.LocalDateTime;
>>>>>>> 3e65d53 (Feat: Añadir las entities que faltaban)
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "publications")
@Getter
@Setter
<<<<<<< HEAD
@NoArgsConstructor

public class Publication {
=======
public class Publication {

>>>>>>> 3e65d53 (Feat: Añadir las entities que faltaban)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

<<<<<<< HEAD
    @Column(length = 2000)
    private String description;

    // Lado propietario: la FK author_id está en la tabla publications
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    // Lado propietario: la FK route_id está en la tabla publications.
    // Sin nullable = false: una publicación puede no llevar ruta.
=======
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_username", nullable = false)
    private User author;

>>>>>>> 3e65d53 (Feat: Añadir las entities que faltaban)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id")
    private Route route;

<<<<<<< HEAD
    // Lado inverso: no crea columnas, solo permite navegar a los comentarios
    @OneToMany(
            mappedBy = "publication",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Comment> comments = new ArrayList<>();

    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setPublication(this);
    }

=======
    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 5000)
    private String content;

    @Lob
    @Column
    private byte[] photo;

    @OneToMany(mappedBy = "publication", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public Publication() {
    }

    public Publication(User author, Route route, String title, String content, byte[] photo, LocalDateTime createdAt) {
        this.author = author;
        this.route = route;
        this.title = title;
        this.content = content;
        this.photo = photo;
        this.createdAt = createdAt;
    }

    @AssertTrue(message = "Una publicación debe incluir al menos una ruta, una foto o comentarios iniciales")
    public boolean isPublicationContentPresent() {
        return route != null || photo != null || content != null;
    }
>>>>>>> 3e65d53 (Feat: Añadir las entities que faltaban)
}
