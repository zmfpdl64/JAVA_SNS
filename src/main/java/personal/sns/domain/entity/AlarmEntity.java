package personal.sns.domain.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;
import org.hibernate.type.descriptor.jdbc.JsonJdbcType;
import personal.sns.domain.AlarmArgs;
import personal.sns.domain.AlarmType;

import java.sql.Timestamp;
import java.time.Instant;


@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name="alarm", indexes = {
        @Index(name="member_id_idx", columnList = "member")
})
@SQLDelete(sql = "UPDATE alarm SET deleted_at = NOW() where id = ?")
@Where(clause = "deleted_at is NULL")
public class AlarmEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "member")
    private MemberEntity member;

    @Enumerated(EnumType.STRING)
    private AlarmType alarmType;

    @Embedded
    private AlarmArgs alarmArgs;

    @Column(name="created_at")
    private Timestamp created_at;

    @Column(name="modified_at")
    private Timestamp modified_at;

    @Column(name="deleted_at")
    private Timestamp deleted_at;

    @PrePersist
    void createAt(){
        this.created_at = Timestamp.from(Instant.now());
    }
    @UpdateTimestamp
    void updateAt() {
        this.modified_at = Timestamp.from(Instant.now());
    }

    public static AlarmEntity of(AlarmArgs args, AlarmType type, MemberEntity member) {
        AlarmEntity alarmEntity = new AlarmEntity();
        alarmEntity.setAlarmArgs(args);
        alarmEntity.setAlarmType(type);
        alarmEntity.setMember(member);
        return alarmEntity;
    }

}
