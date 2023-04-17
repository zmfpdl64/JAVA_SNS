package personal.sns.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import personal.sns.domain.Post;
import personal.sns.domain.entity.MemberEntity;
import personal.sns.domain.entity.PostEntity;
import personal.sns.exception.Errorcode;
import personal.sns.exception.SnsException;
import personal.sns.repository.MemberRepository;
import personal.sns.repository.PostEntityRepository;

import java.util.Objects;

@RequiredArgsConstructor
@Slf4j
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

    @Transactional
    public Post modify(String title, String body, String username, Integer postId) {
        // 포스트 찾기
        log.info("포스트 찾기");
        PostEntity postEntity = postRepository.findById(postId).orElseThrow(() -> new SnsException(Errorcode.NOT_EXISTS_POST, String.format("게시글 ID: %d", postId)));
        // 생성자, 수정자 일치 확인
        log.info("생성자 수정자 일치");
        if (!Objects.equals(postEntity.getMember().getName(), username)){
            throw new SnsException(Errorcode.INVALID_PERMISSION, String.format("생성자: %s \n 수정자: %s", postEntity.getMember().getName(), username));
        }
        postEntity.setTitle(title);
        postEntity.setBody(body);
        PostEntity save = postRepository.save(postEntity);

        return Post.fromEntity(save);
    }


}
