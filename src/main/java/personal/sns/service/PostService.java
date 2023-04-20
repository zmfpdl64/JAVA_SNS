package personal.sns.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import personal.sns.domain.Post;
import personal.sns.domain.entity.LikeEntity;
import personal.sns.domain.entity.MemberEntity;
import personal.sns.domain.entity.PostEntity;
import personal.sns.exception.Errorcode;
import personal.sns.exception.SnsException;
import personal.sns.repository.LikeEntityRepository;
import personal.sns.repository.MemberRepository;
import personal.sns.repository.PostEntityRepository;

import java.util.Objects;

@RequiredArgsConstructor
@Slf4j
@Service
public class PostService {
    private final PostEntityRepository postRepository;
    private final MemberRepository memberRepository;
    private final LikeEntityRepository likeRepository;

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


    public void delete(Integer postId, String username) {
        // 유저 찾기
        MemberEntity memberEntity = memberRepository.findByName(username).orElseThrow(() -> new SnsException(Errorcode.NOT_EXISTS_USERNAME, String.format("유저이름: %s", username)));

        // 게시글 찾기
        PostEntity postEntity = postRepository.findById(postId).orElseThrow(() -> new SnsException(Errorcode.NOT_EXISTS_POST, String.format("포스트 Id: %d", postId)));

        // 유저.이름 == 게시글.유저.이름
        if(!(Objects.equals(memberEntity.getName(), postEntity.getMember().getName()))){
            new SnsException(Errorcode.INVALID_PERMISSION);
        }
        postRepository.delete(postEntity);
    }

    public Page<Post> getList(Pageable pageable) {
        return postRepository.findAll(pageable).map(Post::fromEntity);
    }

    public Page<Post> getMyPost(Pageable pageable, String username) {
        return postRepository.findByMemberName(pageable, username).map(Post::fromEntity);
    }

    public void like(Integer postId, String username) {
        MemberEntity memberEntity = memberRepository.findByName(username).orElseThrow(() -> new SnsException(Errorcode.NOT_EXISTS_USERNAME, String.format("유저이름: %s", username)));

        PostEntity postEntity = postRepository.findById(postId).orElseThrow(() -> new SnsException(Errorcode.NOT_EXISTS_POST, String.format("포스트 Id: %d", postId)));

        likeRepository.findByMemberAndPost(memberEntity, postEntity).ifPresent((it) -> {
            throw new SnsException(Errorcode.ALREADY_LIKE, String.format("유저: %s는 게시글에 이미 좋아요를 눌렀습니다.", username));
        });
        LikeEntity likeEntity = LikeEntity.of(memberEntity, postEntity);
        likeRepository.save(likeEntity);
    }

    public Integer likeCount(Integer postId) {
        //게시글 찾기
        PostEntity postEntity = postRepository.findById(postId).orElseThrow(() -> new SnsException(Errorcode.NOT_EXISTS_POST, String.format("포스트 Id: %d", postId)));

        //좋아요 갯수 가져오기
        Integer count = likeRepository.findByPostCount(postEntity);

        return count;
    }
}
