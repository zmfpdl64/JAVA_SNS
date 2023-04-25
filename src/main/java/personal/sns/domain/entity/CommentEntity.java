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
@Table(name="comment", indexes = {
        @Index(name="comment_idx", columnList = "comment"),
        @Index(name="post_idx", columnList = "post")
})
@SQLDelete(sql = "UPDATE post SET deleted_at = NOW() where id = ?")
@Where(clause = "deleted_at is NULL")
public class CommentEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="comment")
    private String comment;

    @ManyToOne
    @JoinColumn(name = "post")
    private PostEntity post;

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

    public static CommentEntity of(String comment, PostEntity post, MemberEntity member) {
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setComment(comment);
        commentEntity.setPost(post);
        commentEntity.setMember(member);

        return commentEntity;
    }

}
