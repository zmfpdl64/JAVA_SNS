package personal.sns.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import personal.sns.domain.entity.AlarmEntity;
import personal.sns.domain.entity.MemberEntity;


public interface AlarmEntityRepository extends JpaRepository<AlarmEntity, Integer> {

    @Query(value="select a from AlarmEntity a where a.alarmArgs.fromUserId = :member_id")
    Page<AlarmEntity> findAllByMemberId(@Param("member_id") Integer member_id, Pageable pageable);
}
