package personal.sns.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;
import personal.sns.domain.entity.MemberEntity;
import personal.sns.domain.entity.PostEntity;

import java.sql.Timestamp;
import java.time.Instant;

@Setter
@Getter
@AllArgsConstructor
public class Post {

    private Integer id;
    private String title;
    private String body;
    private Member member;
    private Timestamp created_at;
    private Timestamp modified_at;
    private Timestamp deleted_at;

    public static Post fromEntity(PostEntity postEntity) {
        return new Post(
                postEntity.getId(),
                postEntity.getTitle(),
                postEntity.getBody(),
                Member.fromEntity(postEntity.getMember()),
                postEntity.getCreated_at(),
                postEntity.getModified_at(),
                postEntity.getDeleted_at()
        );
    }
}
