package personal.sns.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import personal.sns.domain.Post;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PostResponse {
    private Integer id;
    private String title;
    private String body;
    private MemberResponse memberResponse;
    private Timestamp created_at;
    private Timestamp modified_at;

    public static PostResponse fromPost(Post post){
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getBody(),
                MemberResponse.fromMember(post.getMember()),
                post.getCreated_at(),
                post.getModified_at()
        );
    }
}
