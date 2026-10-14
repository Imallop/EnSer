package gal.usc.etse.es.motorhome.repository;

import gal.usc.etse.es.motorhome.model.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
