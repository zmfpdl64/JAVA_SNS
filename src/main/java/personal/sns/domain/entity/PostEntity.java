package personal.sns.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import java.sql.Timestamp;
import java.time.Instant;


@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name="post", indexes = {
        @Index(name="member_idx", columnList = "member")
})
@SQLDelete(sql = "UPDATE post SET deleted_at = NOW() where id = ?")
@Where(clause = "deleted_at is NULL")
public class PostEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="title")
    private String title;

    @Column(name="body", columnDefinition = "TEXT")
    private String body;

    @ManyToOne
    @JoinColumn(name = "member")
    private MemberEntity member;

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

    public static PostEntity of(String title, String body, MemberEntity member) {
        PostEntity postEntity = new PostEntity();
        postEntity.setTitle(title);
        postEntity.setBody(body);
        postEntity.setMember(member);
        return postEntity;
    }

}
