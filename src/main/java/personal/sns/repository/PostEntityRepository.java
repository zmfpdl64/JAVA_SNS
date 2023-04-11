package personal.sns.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import personal.sns.domain.entity.PostEntity;

import java.util.Optional;

public interface PostEntityRepository extends JpaRepository<PostEntity, Integer> {

}
