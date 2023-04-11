package personal.sns.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import personal.sns.domain.entity.MemberEntity;
import personal.sns.domain.entity.PostEntity;
import personal.sns.exception.Errorcode;
import personal.sns.exception.SnsException;
import personal.sns.repository.MemberRepository;
import personal.sns.repository.PostEntityRepository;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostEntityRepository postRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void create(String title, String body, String username) {
        // 유저 찾기
        MemberEntity member = memberRepository.findByName(username)
                .orElseThrow(() ->new SnsException(Errorcode.NOT_EXISTS_USERNAME, String.format("유저의 이름 %s", username)));
        // 포스트 저장
        PostEntity postEntity = PostEntity.of(title, body, member);
        postRepository.save(postEntity);
    }

}
