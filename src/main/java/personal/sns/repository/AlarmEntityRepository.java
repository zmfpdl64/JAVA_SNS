package personal.sns.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import personal.sns.domain.AlarmType;
import personal.sns.domain.entity.AlarmEntity;
import personal.sns.domain.entity.MemberEntity;
import personal.sns.domain.entity.PostEntity;


public interface AlarmEntityRepository extends JpaRepository<AlarmEntity, Integer> {

    @Query(value="select a from AlarmEntity a where a.alarmArgs.fromUserId = :member_id")
    Page<AlarmEntity> findAllByMemberId(@Param("member_id") Integer member_id, Pageable pageable);

    @Transactional
    @Modifying
    @Query(value = "update AlarmEntity  alarm set alarm.deleted_at = now() " +
            "where alarm.alarmArgs.targetId = :postId")
    void deleteAllByPostId(@Param("postId") Integer postId);


}
