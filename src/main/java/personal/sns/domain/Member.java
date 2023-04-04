package personal.sns.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import personal.sns.domain.entity.MemberEntity;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class Member {
    private Integer memberId;
    private String name;
    private String password;
    private Timestamp createdAt;
    private Timestamp modifiedAt;

    public static Member fromEntity(MemberEntity member) {
        return new Member(
                member.getId(),
                member.getName(),
                member.getPassword(),
                member.getCreated_at(),
                member.getModified_at()
        );
    }
}
