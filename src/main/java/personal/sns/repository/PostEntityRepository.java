package personal.sns.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import personal.sns.domain.entity.PostEntity;

import java.util.Optional;

public interface PostEntityRepository extends JpaRepository<PostEntity, Integer> {
    @Query(value = "select p from PostEntity p where p.member.name = :username")
    Page<PostEntity> findByMemberName(Pageable pageable,@Param("username") String username);

    @Transactional
    @Modifying
    @Query(value = "update PostEntity entity set entity.deleted_at = now() where entity = :post")
    void deleteByPost(@Param("post") PostEntity post);
}
