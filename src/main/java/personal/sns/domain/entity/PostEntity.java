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

@Getter
@Setter
@NoArgsConstructor
@Table(name="\"post\"")
@Entity
@SQLDelete(sql = "UPDATE post SET deleted_at = NOW() where id = ?") //TODO: 이거 살짝 헷갈림
@Where(clause = "deleted_at is NULL")
public class PostEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="title")
    private String title;

    @Column(name="body", columnDefinition = "TEXT")
    private String body;

    @ManyToOne
    @JoinColumn(name="member_id")
    private MemberEntity member;

    private Timestamp created_at;

    private Timestamp modified_at;

    private Timestamp deleted_at;


    @PrePersist
    public void setCreatedAt(){
        this.created_at = Timestamp.from(Instant.now());
    }

    @UpdateTimestamp
    public void setUpdatedAt() {
        this.modified_at = Timestamp.from(Instant.now());
    }

    public static PostEntity of(String title, String body, MemberEntity member) {
        PostEntity post = new PostEntity();
        post.setTitle(title);
        post.setBody(body);
        post.setMember(member);
        return post;
    }
}
