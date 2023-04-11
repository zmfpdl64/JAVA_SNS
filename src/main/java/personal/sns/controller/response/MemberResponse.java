package personal.sns.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import personal.sns.domain.Member;
import personal.sns.domain.MemberRole;

@Getter
@AllArgsConstructor
public class MemberResponse {
    private Integer id;
    private String userName;
    private MemberRole role;

    public static MemberResponse fromMember(Member member) {
        return new MemberResponse(
                member.getMemberId(),
                member.getUsername(),
                member.getRole());
    }
}
