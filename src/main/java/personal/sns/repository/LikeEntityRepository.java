package personal.sns.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import personal.sns.domain.entity.LikeEntity;
import personal.sns.domain.entity.MemberEntity;
import personal.sns.domain.entity.PostEntity;

import java.util.Optional;

public interface LikeEntityRepository extends JpaRepository<LikeEntity, Integer> {

    Optional<LikeEntity> findByMemberAndPost(MemberEntity memberEntity, PostEntity postEntity);

    @Query(value="select COUNT(l) from LikeEntity l where l.post = :postEntity")
    Integer findByPostCount(@Param("postEntity")PostEntity postEntity);

    @Transactional
    @Modifying
    @Query(value = "update LikeEntity entity set entity.deleted_at = now() where entity.post = :post")
    void deleteAllByPost(@Param("post") PostEntity post);
}
