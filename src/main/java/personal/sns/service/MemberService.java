package personal.sns.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import personal.sns.domain.Member;
import personal.sns.domain.entity.MemberEntity;
import personal.sns.exception.Errorcode;
import personal.sns.exception.SnsException;
import personal.sns.repository.MemberRepository;

@Transactional
@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder encoder;

    public Member join(String username, String password) {
        memberRepository.findByName(username).ifPresent(findmember -> {
            throw new SnsException(Errorcode.DUPLICATE_USERNAME, String.format("username is %s", username));
        });

        MemberEntity member = memberRepository.save(MemberEntity.of(username, encoder.encode(password)));

        return Member.fromEntity(member);
    }

    public Member login(String username, String password) {
        MemberEntity member = memberRepository.findByName(username).orElseThrow(() -> new SnsException(Errorcode.NOT_MATCH_AUTH));
        if (!member.getPassword().equals(password)){
            throw new SnsException(Errorcode.NOT_MATCH_AUTH);
        }
        return Member.fromEntity(member);
    }




}
