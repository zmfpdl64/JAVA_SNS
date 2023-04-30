package personal.sns.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import personal.sns.domain.entity.CommentEntity;
import personal.sns.domain.entity.MemberEntity;
import personal.sns.domain.entity.PostEntity;

public interface CommentEntityRepository extends JpaRepository<CommentEntity, Integer> {
    @Query(value = "select c from CommentEntity c where c.post = :post")
    Page<CommentEntity> findbyPost(@Param("post") PostEntity post, Pageable pageable);

    @Query(value = "select c from CommentEntity c where c.member = :member")
    Page<CommentEntity> findByMember(@Param("member") MemberEntity member, Pageable pageable);

    @Transactional
    @Modifying
    @Query(value = "update CommentEntity entity set entity.deleted_at = now() where entity.post = :post")
    void deleteAllByPost(@Param("post") PostEntity post);
}
