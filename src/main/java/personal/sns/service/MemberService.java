package personal.sns.domain;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import personal.sns.domain.entity.MemberEntity;
import personal.sns.domain.exception.Errorcode;
import personal.sns.domain.exception.SnsException;
import personal.sns.repository.MemberRepository;

@Transactional
@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public Member join(String username, String password) {
        memberRepository.findByName(username).ifPresent(findmember -> {
            throw new SnsException(Errorcode.DUPLICATE_USERNAME, String.format("username is %s", username));
        });

        MemberEntity member = memberRepository.save(MemberEntity.of(username, password));

        return Member.fromEntity(member);
    }




}