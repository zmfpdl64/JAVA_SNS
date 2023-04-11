package personal.sns.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import personal.sns.domain.entity.MemberEntity;
import personal.sns.domain.entity.PostEntity;
import personal.sns.exception.Errorcode;
import personal.sns.exception.SnsException;
import personal.sns.repository.MemberRepository;
import personal.sns.repository.PostRepository;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public void create(String title, String body, String username) {
        MemberEntity member = memberRepository.findByName(username).orElseThrow(() ->
                new SnsException(Errorcode.NOT_EXISTS_USERNAME, String.format("유저 이름: %s", username))
        );
        PostEntity post = PostEntity.of(title, body, member);
        postRepository.save(post);
    }
}
