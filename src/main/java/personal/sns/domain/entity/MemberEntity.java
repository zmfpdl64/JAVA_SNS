package personal.sns.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@Table(name="member")
@Entity
public class MemberEntity {

    @Id
    @Column(name="member_id")
    private Integer id;

    private String name;

    private String password;

    private Timestamp created_at;

    private Timestamp modified_at;

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
