package personal.sns.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import personal.sns.domain.entity.LikeEntity;
import personal.sns.domain.entity.MemberEntity;
import personal.sns.domain.entity.PostEntity;

import java.util.Optional;

public interface LikeEntityRepository extends JpaRepository<LikeEntity, Integer> {

    Optional<LikeEntity> findByMemberAndPost(MemberEntity memberEntity, PostEntity postEntity);
}
