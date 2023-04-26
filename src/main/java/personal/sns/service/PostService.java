package personal.sns.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import personal.sns.domain.AlarmArgs;
import personal.sns.domain.AlarmType;
import personal.sns.domain.Comment;
import personal.sns.domain.Post;
import personal.sns.domain.entity.*;
import personal.sns.exception.Errorcode;
import personal.sns.exception.SnsException;
import personal.sns.repository.*;

import java.util.Objects;

@RequiredArgsConstructor
@Slf4j
@Service
public class PostService {
    private final PostEntityRepository postRepository;
    private final MemberRepository memberRepository;
    private final LikeEntityRepository likeRepository;
    private final CommentEntityRepository commentRepository;
    private final AlarmEntityRepository alarmRepository;

    @Transactional
    public void create(String title, String body, String username) {
        // 유저 찾기
        MemberEntity member = getMemberEntity(username);
        // 포스트 저장
        PostEntity postEntity = PostEntity.of(title, body, member);
        postRepository.save(postEntity);
    }

    @Transactional
    public Post modify(String title, String body, String username, Integer postId) {
        // 포스트 찾기
        log.info("포스트 찾기");
        PostEntity postEntity = getPostEntity(postId);
        // 생성자, 수정자 일치 확인
        log.info("생성자 수정자 일치");
        if (!Objects.equals(postEntity.getMember().getName(), username)){
            throw new SnsException(Errorcode.INVALID_PERMISSION, String.format("생성자: %s \n 수정자: %s", postEntity.getMember().getName(), username));
        }
        postEntity.setTitle(title);
        postEntity.setBody(body);
        PostEntity save = postRepository.saveAndFlush(postEntity);

        return Post.fromEntity(save);
    }


    @Transactional
    public void delete(Integer postId, String username) {
        // 유저 찾기
        MemberEntity memberEntity = getMemberEntity(username);

        // 게시글 찾기
        PostEntity postEntity = getPostEntity(postId);

        // 유저.이름 == 게시글.유저.이름
        if(!(Objects.equals(memberEntity.getName(), postEntity.getMember().getName()))){
            throw new SnsException(Errorcode.INVALID_PERMISSION);
        }
        postRepository.delete(postEntity);
    }

    public Page<Post> getList(Pageable pageable) {
        return postRepository.findAll(pageable).map(Post::fromEntity);
    }

    public Page<Post> getMyPost(Pageable pageable, String username) {
        return postRepository.findByMemberName(pageable, username).map(Post::fromEntity);
    }

    @Transactional
    public void like(Integer postId, String username) {
        MemberEntity memberEntity = getMemberEntity(username);

        PostEntity postEntity = getPostEntity(postId);

        likeRepository.findByMemberAndPost(memberEntity, postEntity).ifPresent((it) -> {
            throw new SnsException(Errorcode.ALREADY_LIKE, String.format("유저: %s는 게시글에 이미 좋아요를 눌렀습니다.", username));
        });


        LikeEntity likeEntity = LikeEntity.of(memberEntity, postEntity);
        likeRepository.save(likeEntity);
        //알람 생성
        // 게시글 작성자 != 좋아요 작성자
        if(!Objects.equals(memberEntity, postEntity.getMember())){
            AlarmArgs args = new AlarmArgs(postEntity.getMember().getId(), postEntity.getId());;
            AlarmType type = AlarmType.NEW_LIKE_ON_POST;
            AlarmEntity alarm = AlarmEntity.of(args, type, memberEntity);
            alarmRepository.save(alarm);
        }
    }

    public Integer likeCount(Integer postId) {
        //게시글 찾기
        PostEntity postEntity = getPostEntity(postId);

        //좋아요 갯수 가져오기
        Integer count = likeRepository.findByPostCount(postEntity);

        return count;
    }

    public void createComment(String comment, Integer postId, String username) {
        //유저 찾기
        MemberEntity memberEntity = getMemberEntity(username);

        //게시글 찾기
        PostEntity postEntity = getPostEntity(postId);
        
        //댓글 저장
        CommentEntity commentEntity = CommentEntity.of(comment, postEntity, memberEntity);

        commentRepository.save(commentEntity);
        //알람 생성
        // 게시글 작성자 != 댓글 작성자
        if(!Objects.equals(memberEntity, postEntity.getMember())){
            AlarmArgs args = new AlarmArgs(postEntity.getMember().getId(), postEntity.getId());;
            AlarmType type = AlarmType.NEW_COMMENT_ON_POST;
            AlarmEntity alarm = AlarmEntity.of(args, type, memberEntity);
            alarmRepository.save(alarm);
            log.info("알람 생성");
            return ;
        }
        log.info("알람 생성 X 작성자 ID: {}  알람 발생자 ID: {}", postEntity.getMember().getId(), memberEntity.getId());
    }

    public Page<Comment> getComments(Integer postId, String username, Pageable pageable){
        //유저 찾기
        MemberEntity memberEntity = getMemberEntity(username);
        //게시글 찾기
        PostEntity postEntity = getPostEntity(postId);
        //댓글 조회하기
        Page<Comment> comments = commentRepository.findbyPost(postEntity, pageable).map(Comment::fromEntity);
        //반환하기
        return comments;

    }

    public Page<Comment> getMyComments(String username, Pageable pageable) {
        //유저 찾기
        MemberEntity memberEntity = getMemberEntity(username);
        //댓글 가져오기
        Page<Comment> comments = commentRepository.findByMember(memberEntity, pageable).map(Comment::fromEntity);
        //반환
        return comments;
    }

    private MemberEntity getMemberEntity(String username) {
        return memberRepository.findByName(username).orElseThrow(() -> new SnsException(Errorcode.NOT_EXISTS_USERNAME, String.format("유저이름: %s", username)));
    }
    private PostEntity getPostEntity(Integer postId) {
        return postRepository.findById(postId).orElseThrow(() -> new SnsException(Errorcode.NOT_EXISTS_POST, String.format("포스트 Id: %d", postId)));
    }
}
