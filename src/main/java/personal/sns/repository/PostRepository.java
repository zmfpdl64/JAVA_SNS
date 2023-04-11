package personal.sns.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import personal.sns.domain.entity.PostEntity;

public interface PostRepository extends JpaRepository<PostEntity, Integer> {
}
