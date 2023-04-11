package personal.sns.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;
import personal.sns.domain.MemberRole;

import java.sql.Timestamp;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@Table(name="member")
@Entity
@SQLDelete(sql = "UPDATE user SET deleted_at = NOW() where id = ?")
@Where(clause = "deleted_at is NULL")
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="member_id")
    private Integer id;

    @Column(name="name")
    private String name;

    @Column(name="password")
    private String password;

    @Column(name="role")
    @Enumerated(EnumType.STRING)
    private MemberRole role = MemberRole.MEMBER;

    @Column(name="created_at")
    private Timestamp created_at;

    @Column(name="modified_at")
    private Timestamp modified_at;

    @Column(name="deleted_at")
    private Timestamp deleted_at;

    public static MemberEntity of(String username, String password) {
        MemberEntity member = new MemberEntity();
        member.setName(username);
        member.setPassword(password);
        return member;
    }

    @PrePersist
    void createAt () {
        this.created_at = Timestamp.from(Instant.now());
    }
    @UpdateTimestamp
    void updateAt() {
        this.modified_at = Timestamp.from(Instant.now());
    }

}
