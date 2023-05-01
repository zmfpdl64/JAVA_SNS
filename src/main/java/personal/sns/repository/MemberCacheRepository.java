package personal.sns.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import personal.sns.domain.Member;

import java.time.Duration;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MemberCacheRepository {

    private final RedisTemplate<String, Member> template;
    private final Duration expiredTime = Duration.ofDays(3);

    public void setMember(Member member) {
        String key = getKey(member.getName());
        log.info("Set Member Redis {} : {}", key, member);
        template.opsForValue().set(key, member, expiredTime);
    }

    public Optional<Member> getMember(String username) {
        Member member = template.opsForValue().get(getKey(username));
        log.info("Get member Redis: {}", username);
        return Optional.ofNullable(member);
    }

    private String getKey(String userName){
        return "UID:"+userName;
    }

}
