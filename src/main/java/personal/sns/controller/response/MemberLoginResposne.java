package personal.sns.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import personal.sns.domain.Member;

@Getter
@AllArgsConstructor
public class MemberLoginResposne {
    private String username;
    private String password;
}
