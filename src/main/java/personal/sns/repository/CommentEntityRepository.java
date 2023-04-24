package personal.sns.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import personal.sns.domain.entity.CommentEntity;
import personal.sns.domain.entity.PostEntity;

public interface CommentEntityRepository extends JpaRepository<CommentEntity, Integer> {
    @Query(value = "select c from CommentEntity c where c.post = :post")
    Page<CommentEntity> findbyPost(@Param("post") PostEntity post, Pageable pageable);
}
