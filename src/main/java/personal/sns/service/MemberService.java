package personal.sns.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import personal.sns.domain.Member;
import personal.sns.domain.entity.MemberEntity;
import personal.sns.exception.Errorcode;
import personal.sns.exception.SnsException;
import personal.sns.repository.MemberRepository;
import personal.sns.util.JwtTokenUtils;

@Transactional
@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.token.expired-time-ms}")
    private Long expiredTimeMs;

    public Member join(String username, String password) {
        memberRepository.findByName(username).ifPresent(findmember -> {
            throw new SnsException(Errorcode.DUPLICATE_USERNAME, String.format("유저 아이디: %s", username));
        });

        MemberEntity member = memberRepository.save(MemberEntity.of(username, encoder.encode(password)));

        return Member.fromEntity(member);
    }

    public String login(String username, String password) {
        // 회원가입 여부 체크
        MemberEntity member = memberRepository.findByName(username).orElseThrow(() -> new SnsException(Errorcode.NOT_EXISTS_USERNAME, String.format("%s를 찾지 못했습니다.", username)));
        // 비밀번호 체크
        if (!encoder.matches(password, member.getPassword())){
            throw new SnsException(Errorcode.NOT_MATCH_PASSWORD);
        }
        // 토큰 생성
        return JwtTokenUtils.generateToken(username, secretKey, expiredTimeMs);
    }




}
