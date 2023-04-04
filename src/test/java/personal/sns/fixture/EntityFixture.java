package personal.sns.fixture;

import personal.sns.domain.entity.MemberEntity;

import java.sql.Timestamp;
import java.time.Instant;

public class EntityFixture {

    public static MemberEntity of(String username, String password) {
        MemberEntity member = MemberEntity.of(username, password);
        member.setId(1);
        member.setCreated_at(Timestamp.from(Instant.now()));
        member.setModified_at(Timestamp.from(Instant.now()));

        return member;
    }
}
