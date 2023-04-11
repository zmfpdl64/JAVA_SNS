package personal.sns.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import personal.sns.domain.Member;
import personal.sns.domain.MemberRole;

@Getter
@AllArgsConstructor
public class MemberJoinResponse {
    private Integer userId;
    private String userName;
    private MemberRole role;

    public static MemberJoinResponse fromMember(Member member){
        return new MemberJoinResponse(member.getMemberId(), member.getName(), member.getRole());
    }

}
