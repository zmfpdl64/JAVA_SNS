package personal.sns.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import personal.sns.domain.entity.PostEntity;

import java.util.Optional;

public interface PostEntityRepository extends JpaRepository<PostEntity, Integer> {
    @Query("select p from PostEntity p where p.member.name = :username")
    Page<PostEntity> findByMemberName(Pageable pageable, String username);
}
