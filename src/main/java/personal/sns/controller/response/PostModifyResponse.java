package personal.sns.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import personal.sns.domain.Post;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class PostModifyResponse {
    private Integer id;
    private String title;
    private String body;
    private MemberResponse member;
    private Timestamp created_at;
    private Timestamp modified_at;

    public static PostModifyResponse fromPost(Post post){
        return new PostModifyResponse(
                post.getId(),
                post.getTitle(),
                post.getBody(),
                MemberResponse.fromMember(post.getMember()),
                post.getCreated_at(),
                post.getModified_at()
        );
    }
}
