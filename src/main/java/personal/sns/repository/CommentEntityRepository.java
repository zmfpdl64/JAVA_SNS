package personal.sns.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import personal.sns.domain.entity.CommentEntity;

public interface CommentEntityRepository extends JpaRepository<CommentEntity, Integer> {
}
