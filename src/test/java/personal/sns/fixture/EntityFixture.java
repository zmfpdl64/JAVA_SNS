package personal.sns.fixture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import personal.sns.domain.entity.MemberEntity;
import personal.sns.domain.entity.PostEntity;

import java.sql.Timestamp;
import java.time.Instant;

public class EntityFixture {

    public static Integer memberId1 = 1;
    public static Integer memberId2 = 2;
    public static String username1 = "username";
    public static String username2 = "username2";
    public static String password = "password";
    public static String password1 = "password1";

    public static Integer PostId1 = 1;

    public static Integer PostId2 = 2;



    public static MemberEntity of(String username, String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        MemberEntity member = MemberEntity.of(username, encoder.encode(password));
        member.setId(1);
        member.setCreated_at(Timestamp.from(Instant.now()));
        member.setModified_at(Timestamp.from(Instant.now()));
        return member;
    }
    public static MemberEntity of(String username, String password, Integer id) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        MemberEntity member = MemberEntity.of(username, encoder.encode(password));
        member.setId(id);
        member.setCreated_at(Timestamp.from(Instant.now()));
        member.setModified_at(Timestamp.from(Instant.now()));
        return member;
    }
    public static MemberEntity of() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        MemberEntity member = MemberEntity.of("username", encoder.encode("password"));
        member.setId(1);
        member.setCreated_at(Timestamp.from(Instant.now()));
        member.setModified_at(Timestamp.from(Instant.now()));
        return member;
    }

    public static PostEntity getPost1(String title, String body) {
        MemberEntity member = of("username", "password", 1);
        PostEntity postEntity = new PostEntity();

        postEntity.setTitle(title);
        postEntity.setBody(body);
        postEntity.setId(1);
        postEntity.setMember(member);
        return postEntity;

    }
}
