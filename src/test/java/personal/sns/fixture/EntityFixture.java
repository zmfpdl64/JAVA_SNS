package personal.sns.fixture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import personal.sns.domain.entity.MemberEntity;

import java.sql.Timestamp;
import java.time.Instant;

public class EntityFixture {

    public static MemberEntity of(String username, String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        MemberEntity member = MemberEntity.of(username, encoder.encode(password));
        member.setId(1);
        member.setCreated_at(Timestamp.from(Instant.now()));
        member.setModified_at(Timestamp.from(Instant.now()));

        return member;
    }
}
