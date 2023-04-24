package personal.sns.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import personal.sns.domain.Comment;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CommentResponse {
    private Integer id;
    private String comment;
    private String username;
    private Timestamp created_at;
    private Timestamp modified_at;

    public static CommentResponse fromComment(Comment comment){
        return new CommentResponse(
                comment.getId(),
                comment.getComment(),
                comment.getUsername(),
                comment.getCreated_at(),
                comment.getModified_at()
        );
    }
}
