package personal.sns.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import personal.sns.domain.entity.CommentEntity;
import personal.sns.domain.entity.PostEntity;

import java.sql.Timestamp;

@Setter
@Getter
@AllArgsConstructor
public class Comment {

    private Integer id;
    private String comment;
    private Integer postId;
    private String username;
    private Timestamp created_at;
    private Timestamp modified_at;

    public static Comment fromEntity(CommentEntity commentEntity) {
        return new Comment(
                commentEntity.getId(),
                commentEntity.getComment(),
                commentEntity.getPost().getId(),
                commentEntity.getMember().getName(),
                commentEntity.getCreated_at(),
                commentEntity.getModified_at()
        );
    }
}