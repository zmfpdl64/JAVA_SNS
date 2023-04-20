package personal.sns.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;
import personal.sns.domain.Member;

import java.sql.Timestamp;
import java.time.Instant;


@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name="\"like\"")
@Where(clause = "deleted_at is NULL")
public class LikeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private MemberEntity member;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private PostEntity post;

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

    public static LikeEntity of(MemberEntity member, PostEntity postEntity) {
        LikeEntity likeEntity = new LikeEntity();
        likeEntity.setMember(member);
        likeEntity.setPost(postEntity);
        return likeEntity;
    }

}