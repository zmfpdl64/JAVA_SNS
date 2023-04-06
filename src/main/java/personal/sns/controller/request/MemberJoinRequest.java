package personal.sns.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import personal.sns.controller.response.MemberJoinResponse;
import personal.sns.domain.Member;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberJoinRequest {
    private String username;
    private String password;
}
