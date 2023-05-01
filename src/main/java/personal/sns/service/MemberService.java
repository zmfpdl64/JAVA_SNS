package personal.sns.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import personal.sns.domain.Alarm;
import personal.sns.domain.Member;
import personal.sns.domain.entity.MemberEntity;
import personal.sns.exception.Errorcode;
import personal.sns.exception.SnsException;
import personal.sns.repository.AlarmEntityRepository;
import personal.sns.repository.MemberCacheRepository;
import personal.sns.repository.MemberRepository;
import personal.sns.util.JwtTokenUtils;


@Transactional
@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberCacheRepository cacheRepository;
    private final AlarmEntityRepository alarmRepository;
    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.token.expired-time-ms}")
    private Long expiredTimeMs;

    public Member loadMemberByMemberName(String userName) {
        return cacheRepository.getMember(userName)
                .orElseGet(
                        () -> memberRepository.findByName(userName).map(Member::fromEntity).orElseThrow(() ->
                                        new SnsException(Errorcode.NOT_EXISTS_USERNAME, String.format("%s not founded", userName)))
                        );

    }

    public Member join(String username, String password) {
        memberRepository.findByName(username).ifPresent(findmember -> {
            throw new SnsException(Errorcode.DUPLICATE_USERNAME, String.format("유저 아이디: %s", username));
        });

        MemberEntity member = memberRepository.save(MemberEntity.of(username, encoder.encode(password)));

        return Member.fromEntity(member);
    }

    public String login(String username, String password) {
        // 회원가입 여부 체크
        Member member = loadMemberByMemberName(username);

        // 비밀번호 체크
        if (!encoder.matches(password, member.getPassword())){
            throw new SnsException(Errorcode.NOT_MATCH_PASSWORD);
        }
        // 멤버 캐싱
        cacheRepository.setMember(member);

        // 토큰 생성
        return JwtTokenUtils.generateToken(username, secretKey, expiredTimeMs);
    }


    public Page<Alarm> myAlarmList(String username, Pageable pageable) {
        //유저 찾기
        MemberEntity memberEntity = getMemberEntity(username);

        //알람 가져오기
        Page<Alarm> alarms = alarmRepository.findAllByMemberId(memberEntity.getId(), pageable).map(Alarm::fromEntity);

        //반환하기
        return alarms;
    }

    private MemberEntity getMemberEntity(String username) {
        return memberRepository.findByName(username).orElseThrow(() -> new SnsException(Errorcode.NOT_EXISTS_USERNAME, String.format("유저이름: %s", username)));
    }
}
