package personal.sns.fixture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import personal.sns.domain.Alarm;
import personal.sns.domain.AlarmArgs;
import personal.sns.domain.AlarmType;
import personal.sns.domain.Member;
import personal.sns.domain.entity.AlarmEntity;
import personal.sns.domain.entity.CommentEntity;
import personal.sns.domain.entity.MemberEntity;
import personal.sns.domain.entity.PostEntity;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

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
    public static PostEntity getPost1() {
        MemberEntity member = of("username", "password", 1);
        PostEntity postEntity = new PostEntity();
        postEntity.setTitle("title");
        postEntity.setBody("body");
        postEntity.setId(1);
        postEntity.setMember(member);
        return postEntity;
    }

    public static CommentEntity getComment(){
        PostEntity post = getPost1("title", "body");
        return CommentEntity.of("comment", post, post.getMember());
    }

    public static AlarmEntity getCommentAlarm() {
        PostEntity post = getPost1("title", "body");
        MemberEntity member = of("username2", "password2", 2);
        AlarmArgs args = new AlarmArgs(member.getId(), post.getMember().getId());
        return AlarmEntity.of(args, AlarmType.NEW_COMMENT_ON_POST, member);
    }
    public static AlarmEntity getLikeAlarm() {
        PostEntity post = getPost1("title", "body");
        MemberEntity member = of("username2", "password2", 2);
        AlarmArgs args = new AlarmArgs(member.getId(), post.getMember().getId());
        return AlarmEntity.of(args, AlarmType.NEW_LIKE_ON_POST, member);
    }

    public static List<Alarm> getAlarms() {
        MemberEntity member = of();
        return List.of(
                new Alarm(
                        1,
                        Member.fromEntity(member),
                        AlarmType.NEW_COMMENT_ON_POST,
                        new AlarmArgs(1, 10),
                        Timestamp.from(Instant.now()),
                        null,
                        null
                ),
                new Alarm(
                        2,
                        Member.fromEntity(member),
                        AlarmType.NEW_COMMENT_ON_POST,
                        new AlarmArgs(1, 10),
                        Timestamp.from(Instant.now()),
                        null,
                        null
                )
        );
    }
}
