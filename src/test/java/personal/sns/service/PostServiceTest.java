package personal.sns.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import personal.sns.domain.entity.*;
import personal.sns.exception.Errorcode;
import personal.sns.exception.SnsException;
import personal.sns.fixture.EntityFixture;
import personal.sns.repository.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@DisplayName("게시글 서비스")
@ActiveProfiles("test")
@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @MockBean
    private PostEntityRepository postRepository;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private LikeEntityRepository likeRepository;

    @MockBean
    private CommentEntityRepository commentRepository;

    @MockBean
    private AlarmEntityRepository alarmRepository;

    @Nested
    @DisplayName("게시글 작성 테스트")
    class 게시글_작성{
        @DisplayName("게시글 작성 로그인상태 성공")
        @Test
        void 게시글_작성_로그인_상태_성공() {
            //Given
            String username = "username";
            String password = "password";
            String title = "title";
            String body = "body";

            //When
            when(memberRepository.findByName(username)).thenReturn(Optional.of(EntityFixture.of()));
            when(postRepository.save(any())).thenReturn(mock(PostEntity.class));

            //Then
            assertDoesNotThrow(() -> postService.create(title, body, username));
        }
        @DisplayName("게시글 작성 로그인_안한_상태 실패")
        @Test
        void 게시글_작성_로그인_안한_상태_실패() {
            //Given
            String username = "username";
            String title = "title";
            String body = "body";

            //When
            when(memberRepository.findByName(username)).thenReturn(Optional.empty());

            //Then
            assertThrows(SnsException.class, () -> {
                postService.create(title, body, username);
            });
        }
    }


    @Nested
    class 게시글_수정 {
        @DisplayName("게시글 수정 로그인상태 성공")
        @Test
        void 게시글_수정_로그인_상태_성공() {
            //Given
            String title = "title";
            String body = "body";
            String username = "username";
            PostEntity post = EntityFixture.getPost1(title, body);
            MemberEntity member = post.getMember();

            //When
            when(memberRepository.findByName(eq(member.getName()))).thenReturn(Optional.of(member));
            when(postRepository.findById(eq(post.getId()))).thenReturn(Optional.of(post));
            when(postRepository.saveAndFlush(post)).thenReturn(post);

            //Then
            assertDoesNotThrow(() ->
                    postService.modify(post.getTitle(), post.getBody(), member.getName(), post.getId())
            );
        }

        @DisplayName("게시글 수정 유저이름 못찾음 실패")
        @Test
        void 게시글_수정_유저이름_못찾음_실패() {
            //Given
            String username = "username";
            String password = "password";
            String title = "title";
            String body = "body";

            //When
            when(memberRepository.findByName(username)).thenReturn(Optional.empty());
            when(postRepository.save(EntityFixture.getPost1(title, body))).thenThrow(new SnsException(Errorcode.NOT_EXISTS_USERNAME));

            //Then
            assertThrows(SnsException.class, () ->
                    postService.modify("modifyTitle", "modifyBody", EntityFixture.username1, EntityFixture.PostId1)
            );
        }

        @DisplayName("게시글 수정 토큰만료 실패")
        @Test
        void 게시글_작성_토큰만료_실패() {
            //Given
            String username = "username";
            String password = "password";
            String title = "title";
            String body = "body";

            //When
            when(memberRepository.findByName(username)).thenReturn(Optional.of(EntityFixture.of()));
            when(postRepository.save(any())).thenReturn(mock(PostEntity.class));

            //Then
            assertThrows(SnsException.class, () ->
                    postService.modify("modifyTitle", "modifyBody", EntityFixture.username1, EntityFixture.PostId1)
            );
        }

        @DisplayName("게시글 수정 생성자 수정자 불일치 실패")
        @Test
        void 게시글_수정_생성자_수정자_불일치_실패() {
            //Given
            String username = "username";
            String password = "password";
            String title = "title";
            String body = "body";

            //When
            when(memberRepository.findByName(username)).thenReturn(Optional.of(EntityFixture.of()));
            when(postRepository.save(EntityFixture.getPost1(title, body))).thenReturn(EntityFixture.getPost1(title, body));


            //Then

            assertThrows(SnsException.class, () ->
                    postService.modify("modifyTitle", "modifyBody", EntityFixture.username1, EntityFixture.PostId1)
            );
        }

        @DisplayName("게시글 수정 게시글 못찾음 실패")
        @Test
        void 게시글_수정_게시글_못찾음_실패() {
            //Given
            String username = "username";
            String password = "password";
            String title = "title";
            String body = "body";

            //When
            when(memberRepository.findByName(username)).thenReturn(Optional.of(EntityFixture.of()));
            when(postRepository.save(any())).thenReturn(mock(PostEntity.class));

            //Then
            assertThrows(SnsException.class, () ->
                    postService.modify("modifyTitle", "modifyBody", EntityFixture.username1, EntityFixture.PostId1)
            );
        }
    }

    @Nested
    @DisplayName("게시글 삭제 테스트")
    class 게시글_삭제{
        @DisplayName("게시글 삭제 로그인상태 성공")
        @Test
        void 게시글_삭제_로그인_상태_성공() {
            //Given
            String title = "title";
            String body = "body";
            PostEntity post = EntityFixture.getPost1(title, body);
            MemberEntity member = post.getMember();

            //When
            when(memberRepository.findByName(eq(member.getName()))).thenReturn(Optional.of(member));
            when(postRepository.findById(eq(post.getId()))).thenReturn(Optional.of(post));
            doNothing().when(postRepository).delete(post);

            //Then
            assertDoesNotThrow(() ->
                    postService.delete(post.getId(), member.getName())
            );
        }

        @DisplayName("게시글 유저 존재 X 실패")
        @Test
        void 게시글_유저_존재X_실패() {
            //Given
            String title = "title";
            String body = "body";
            PostEntity post = EntityFixture.getPost1(title, body);
            MemberEntity member = post.getMember();

            //When
            when(memberRepository.findByName(eq(member.getName()))).thenThrow(new SnsException(Errorcode.NOT_EXISTS_USERNAME));
    //        when(postRepository.findById(eq(post.getId()))).thenReturn(Optional.of(post));
            doNothing().when(postRepository).delete(post);

            //Then
            assertThrows(SnsException.class, () ->
                    postService.delete(post.getId(), member.getName())
            );
        }

        @DisplayName("게시글 게시글 존재 x 실패")
        @Test
        void 게시글_존재X_실패() {
            //Given
            String title = "title";
            String body = "body";
            PostEntity post = EntityFixture.getPost1(title, body);
            MemberEntity member = post.getMember();

            //When
            when(memberRepository.findByName(eq(member.getName()))).thenReturn(Optional.of(member));
            when(postRepository.findById(eq(post.getId()))).thenReturn(Optional.empty());
            doNothing().when(postRepository).delete(post);

            //Then
            assertThrows(SnsException.class, () ->
                    postService.delete(post.getId(), member.getName())
            );
        }

        @DisplayName("게시글 생성자 삭제자 불일치 실패")
        @Test
        void 게시글_생성자_삭제자_불일치_실패() {
            //Given
            String title = "title";
            String body = "body";
            PostEntity post = EntityFixture.getPost1(title, body);

            //When
            when(memberRepository.findByName("deleteusername")).thenReturn(Optional.of(EntityFixture.of("deleteusername", "password")));
            when(postRepository.findById(eq(post.getId()))).thenReturn(Optional.of(post));
            doNothing().when(postRepository).delete(post);

            //Then
            assertThrows(SnsException.class, () ->
                    postService.delete(post.getId(), post.getMember().getName())
            );
        }
    }
    @Nested
    @DisplayName("게시글 목록 테스트")
    class getPostList{

        @DisplayName("게시글 목록 가져오기 성공")
        @Test
        void 게시글_목록_가져오기_성공() {
            //Given
            Pageable page = mock(Pageable.class);

            //When
            when(postRepository.findAll(page)).thenReturn(Page.empty());

            //Then
            assertDoesNotThrow(() -> postService.getList(page));
        }

        @DisplayName("게시글 목록 내 것 가져오기 성공")
        @Test
        void 게시글_목록_내_것_가져오기_성공() {
            //Given
            Pageable pageable = mock(Pageable.class);
            //When
            when(postRepository.findByMemberName(any(), eq("username"))).thenReturn(Page.empty());

            //Then
            assertDoesNotThrow(() -> postService.getMyPost(pageable, "username"));
        }

    }

    @Nested
    @DisplayName("게시글 좋아요 테스트")
    class LikeTest{
        @DisplayName("게시글 좋아요 성공")
        @Test
        void 게시글_좋아요_성공() {
            //Given
            Integer postId = 1;
            String username = "username";
            String password = "password2";
            Integer memberId = 2;
            MemberEntity member = EntityFixture.of(username, password, memberId);
            PostEntity postEntity = EntityFixture.getPost1();

            //When
            when(postRepository.findById(postId)).thenReturn(Optional.of(postEntity));
            when(memberRepository.findByName(username)).thenReturn(Optional.of(member));
            when(likeRepository.findByMemberAndPost(member, postEntity)).thenReturn(Optional.empty());
            when(likeRepository.save(any(LikeEntity.class))).thenReturn(mock(LikeEntity.class));
            when(alarmRepository.save(any(AlarmEntity.class))).thenReturn(mock(AlarmEntity.class));

            //Then
            assertDoesNotThrow(()-> postService.like(postId, username));
        }
        @DisplayName("게시글 좋아요 유저 X 실패")
        @Test
        void 게시글_좋아요_유저X_실패() {
            //Given
            Integer postId = 1;
            String username = "username";

            //When
            when(postRepository.findById(postId)).thenReturn(Optional.of(mock(PostEntity.class)));
            when(memberRepository.findByName(username)).thenReturn(Optional.empty());
            when(likeRepository.findByMemberAndPost(mock(MemberEntity.class), mock(PostEntity.class))).thenReturn(Optional.empty());
            when(likeRepository.save(any(LikeEntity.class))).thenReturn(mock(LikeEntity.class));
            when(alarmRepository.save(any(AlarmEntity.class))).thenReturn(mock(AlarmEntity.class));

            //Then
            assertThrows(SnsException.class, ()-> postService.like(postId, username));
        }

        @DisplayName("게시글 좋아요 이미 좋아요 실패")
        @Test
        void 게시글_좋아요_이미_좋아요_실패() {
            //Given
            Integer postId = 1;
            String username = "username";

            //When
            when(postRepository.findById(postId)).thenReturn(Optional.of(mock(PostEntity.class)));
            when(memberRepository.findByName(username)).thenReturn(Optional.empty());
            when(likeRepository.findByMemberAndPost(mock(MemberEntity.class), mock(PostEntity.class))).thenReturn(Optional.of(mock(LikeEntity.class)));
            when(likeRepository.save(any(LikeEntity.class))).thenReturn(mock(LikeEntity.class));
            when(alarmRepository.save(any(AlarmEntity.class))).thenReturn(mock(AlarmEntity.class));

            //Then
            assertThrows(SnsException.class, ()-> postService.like(postId, username));
        }
    }

    @Nested
    @DisplayName("게시글 좋아요 카운트 테스트")
    class LikeCount{
        @Test
        @DisplayName("게시글 좋아요 카운트 성공")
        void 게시글_좋아요_카운트_성공() {
            //Given
            Integer postId = 1;
            Integer count = 10;
            PostEntity post = EntityFixture.getPost1("title", "body");

            //When

            //게시글 찾기
            when(postRepository.findById(any())).thenReturn(Optional.of(post));
            when(likeRepository.findByPostCount(any())).thenReturn(count);

            //Then
            assertDoesNotThrow(() -> postService.likeCount(any()));
        }

        @Test
        @DisplayName("게시글 좋아요 카운트 게시글 존재X 실패")
        void 게시글_좋아요_카운트_게시글_존재X_실패() {
            //Given
            Integer postId = 1;
            PostEntity post = EntityFixture.getPost1("title", "body");
            //When

            //게시글 찾기
            when(postRepository.findById(eq(postId))).thenReturn(Optional.empty());

            //Then
            assertThrows(SnsException.class, () -> postService.likeCount(postId));
        }

    }

    @Nested
    @DisplayName("댓글 달기 테스트")
    class CreateComment{
        @Test
        @DisplayName("댓글 달기 성공")
        void 댓글_달기_성공() {
            //Given
            Integer postId = 1;
            PostEntity post = EntityFixture.getPost1("title", "body");
            CommentEntity comment = CommentEntity.of("comment", post, post.getMember());
            AlarmEntity commentAlarm = EntityFixture.getCommentAlarm();

            //When
            when(memberRepository.findByName("username")).thenReturn(Optional.of(commentAlarm.getMember()));
            when(postRepository.findById(postId)).thenReturn(Optional.of(post));
            when(commentRepository.save(comment)).thenReturn(comment);
            when(alarmRepository.save(commentAlarm)).thenReturn(commentAlarm);
            //Then
            assertDoesNotThrow(() -> postService.createComment("comment", postId, "username"));
        }

        @Test
        @DisplayName("댓글 달기 유저 존재 X 실패")
        void 댓글_달기_유저_존재X_실패() {
            //Given
            Integer postId = 1;
            PostEntity post = EntityFixture.getPost1("title", "body");
            CommentEntity comment = CommentEntity.of("comment", post, post.getMember());

            //When
            when(memberRepository.findByName("username")).thenReturn(Optional.empty());
            when(postRepository.findById(postId)).thenReturn(Optional.of(post));
            when(commentRepository.save(comment)).thenReturn(comment);
            when(alarmRepository.save(any(AlarmEntity.class))).thenReturn(mock(AlarmEntity.class));

            //Then
            assertThrows(SnsException.class, () -> postService.createComment("comment", postId, "username"));
        }

        @Test
        @DisplayName("댓글 달기 게시글 존재X 실패")
        void 댓글_달기_게시글_존재X_실패() {
            //Given
            Integer postId = 1;
            PostEntity post = EntityFixture.getPost1("title", "body");
            CommentEntity comment = CommentEntity.of("comment", post, post.getMember());

            //When
            when(memberRepository.findByName("username")).thenReturn(Optional.of(post.getMember()));
            when(postRepository.findById(postId)).thenReturn(Optional.empty());
            when(commentRepository.save(comment)).thenReturn(comment);
            when(alarmRepository.save(any(AlarmEntity.class))).thenReturn(mock(AlarmEntity.class));

            //Then
            assertThrows(SnsException.class, () -> postService.createComment("comment", postId, "username"));
        }

    }

    @Nested
    @DisplayName("댓글 조회 테스트")
    class GetComments{
        @Test
        @DisplayName("댓글 조회 성공")
        void 댓글_조회_성공() {
            ///Given
            Integer postId = 1;
            PostEntity post = EntityFixture.getPost1("title", "username");
            MemberEntity member = EntityFixture.of();
            Pageable pageable = mock(Pageable.class);

            //When
            when(memberRepository.findByName(member.getName())).thenReturn(Optional.of(member));
            when(postRepository.findById(postId)).thenReturn(Optional.of(post));
            when(commentRepository.findbyPost(post, pageable)).thenReturn(Page.empty());

            //Then
            assertDoesNotThrow(() -> postService.getComments(postId, member.getName(), pageable));
        }

        @Test
        @DisplayName("댓글 조회 유저 존재X 실패")
        void 댓글_조회_유저_존재X_실패() {
            ///Given
            Integer postId = 1;
            PostEntity post = EntityFixture.getPost1("title", "username");
            MemberEntity member = EntityFixture.of();
            Pageable pageable = mock(Pageable.class);

            //When
            when(memberRepository.findByName(member.getName())).thenReturn(Optional.empty());
            when(postRepository.findById(postId)).thenReturn(Optional.of(post));
            when(commentRepository.findbyPost(post,pageable)).thenReturn(Page.empty());

            //Then
            assertThrows(SnsException.class, () -> postService.getComments(postId, member.getName(), pageable));
        }

        @Test
        @DisplayName("댓글 조회 게시글 존재X 실패")
        void 댓글_조회_게시글_존재X_실패() {
            ///Given
            Integer postId = 1;
            PostEntity post = EntityFixture.getPost1("title", "username");
            MemberEntity member = EntityFixture.of();
            Pageable pageable = mock(Pageable.class);

            //When
            when(memberRepository.findByName(member.getName())).thenReturn(Optional.of(member));
            when(postRepository.findById(postId)).thenReturn(Optional.empty());
            when(commentRepository.findbyPost(post, pageable)).thenReturn(Page.empty());

            //Then
            assertThrows(SnsException.class, () -> postService.getComments(postId, member.getName(), pageable));
        }
    }

    @Nested
    @DisplayName("내 댓글 조회 테스트")
    class MyComments{
        @Test
        @DisplayName("내 댓글 조회 성공")
        void 내_댓글_조회_성공() {
            //Given
            String username ="username";
            MemberEntity member = EntityFixture.of();
            Pageable pageable = mock(Pageable.class);

            //When
            when(memberRepository.findByName(username)).thenReturn(Optional.of(member));
            when(commentRepository.findByMember(member, pageable)).thenReturn(Page.empty());

            //Then
            assertDoesNotThrow(() -> postService.getMyComments(username, pageable));
        }
        @Test
        @DisplayName("내 댓글 조회 유저 존재X 실패")
        void 내_댓글_조회_유저_존재X_실패() {
            //Given
            String username ="username";
            MemberEntity member = EntityFixture.of();
            Pageable pageable = mock(Pageable.class);

            //When
            when(memberRepository.findByName(username)).thenReturn(Optional.empty());
            when(commentRepository.findByMember(member, pageable)).thenReturn(Page.empty());

            //Then
            assertThrows(SnsException.class, () -> postService.getMyComments(username, pageable));
        }
    }

    @Nested
    @DisplayName("댓글 알람 생성 테스트")
    class CreateAlarm {
        @Test
        @DisplayName("댓글알람 생성 성공")
        void 댓글알람_생성_성공() {
            //Given
            Integer postId = 1;
            String username = "username";
            AlarmEntity commentAlarm = EntityFixture.getCommentAlarm();
            PostEntity post = EntityFixture.getPost1();
            CommentEntity comment = EntityFixture.getComment();

            //When
            //유저 찾기
            when(memberRepository.findByName(username)).thenReturn(Optional.of(commentAlarm.getMember()));
            //게시글 찾기
            when(postRepository.findById(postId)).thenReturn(Optional.of(post));
            // 댓글 저장
            when(commentRepository.save(comment)).thenReturn(comment);
            // 알람 생성
            when(alarmRepository.save(commentAlarm)).thenReturn(commentAlarm);

            //Then
            assertDoesNotThrow(() -> postService.createComment("comment", postId, "username"));
        }

        @Test
        @DisplayName("댓글알람 생성 게시글작성자 댓글작성자 일치 실패")
        void 댓글알람_생성_게시글생성자_댓글작성자_일치_실패() {
            //Given
            Integer postId = 1;
            String username = "username";
            AlarmEntity commentAlarm = EntityFixture.getCommentAlarm();
            PostEntity post = EntityFixture.getPost1();
            CommentEntity comment = EntityFixture.getComment();

            //When
            //유저 찾기
            when(memberRepository.findByName(username)).thenReturn(Optional.of(post.getMember()));
            //게시글 찾기
            when(postRepository.findById(postId)).thenReturn(Optional.of(post));
            // 댓글 저장
            when(commentRepository.save(comment)).thenReturn(comment);
            // 알람 생성X



            //Then
            assertDoesNotThrow(() -> postService.createComment("comment", postId, "username"));
        }
        @Test
        @DisplayName("댓글알람 생성 게시글 존재X 실패")
        void 댓글알람_생성_게시글_존재X_실패() {
            //Given
            Integer postId = 1;
            String username = "username";
            AlarmEntity commentAlarm = EntityFixture.getCommentAlarm();
            PostEntity post = EntityFixture.getPost1();
            CommentEntity comment = EntityFixture.getComment();

            //When
            //유저 찾기
            when(memberRepository.findByName(username)).thenReturn(Optional.of(commentAlarm.getMember()));
            //게시글 찾기
            when(postRepository.findById(postId)).thenReturn(Optional.empty());
            // 댓글 저장
            when(commentRepository.save(comment)).thenReturn(comment);
            // 알람 생성
            when(alarmRepository.save(commentAlarm)).thenReturn(commentAlarm);


            //Then
            assertThrows(SnsException.class, () -> postService.createComment("comment", postId, "username"));
        }

        @Test
        @DisplayName("댓글알람 생성 유저 존재X 실패")
        void 댓글알람_생성_유저_존재X_실패() {
            //Given
            Integer postId = 1;
            String username = "username";

            AlarmEntity commentAlarm = EntityFixture.getCommentAlarm();
            PostEntity post = EntityFixture.getPost1();
            CommentEntity comment = EntityFixture.getComment();

            //When
            //유저 찾기
            when(memberRepository.findByName(username)).thenReturn(Optional.empty());
            //게시글 찾기
            when(postRepository.findById(postId)).thenReturn(Optional.empty());
            // 댓글 저장
            when(commentRepository.save(comment)).thenReturn(comment);
            // 알람 생성
            when(alarmRepository.save(commentAlarm)).thenReturn(commentAlarm);


            //Then
            assertThrows(SnsException.class, () -> postService.createComment("comment", postId, "username"));
        }
    }

    @Nested
    @DisplayName("좋아요 알림 테스트")
    class LikAlarmTest{
        @Test
        @DisplayName("좋아요 알람 성공")
        void 좋아요_알람_성공() {
            //Given
            Integer postId = 1;
            String username = "username";
            MemberEntity member = EntityFixture.of(username, "password2", 2);
            AlarmEntity likeAlarm = EntityFixture.getLikeAlarm();
            PostEntity post = EntityFixture.getPost1();

            //When
            when(postRepository.findById(postId)).thenReturn(Optional.of(post));
            when(memberRepository.findByName(username)).thenReturn(Optional.of(member));
            when(likeRepository.findByMemberAndPost(member, post)).thenReturn(Optional.empty());
            when(likeRepository.save(any(LikeEntity.class))).thenReturn(mock(LikeEntity.class));
            when(alarmRepository.save(likeAlarm)).thenReturn(likeAlarm);

            //Then
            assertDoesNotThrow(()-> postService.like(postId, username));
        }

        @Test
        @DisplayName("좋아요 알람 게시글작성자 좋아요작성자 일치 알람생성X")
        void 좋아요알람_게시글작성자_좋아요작성자_일치_알람생성X() {
            //Given
            Integer postId = 1;
            String username = "username";
            PostEntity post = EntityFixture.getPost1();
            AlarmEntity likeAlarm = EntityFixture.getLikeAlarm();

            //When
            when(postRepository.findById(postId)).thenReturn(Optional.of(post));
            when(memberRepository.findByName(username)).thenReturn(Optional.of(post.getMember()));
            when(likeRepository.findByMemberAndPost(mock(MemberEntity.class), mock(PostEntity.class))).thenReturn(Optional.empty());
            when(likeRepository.save(any(LikeEntity.class))).thenReturn(mock(LikeEntity.class));


            //Then
            assertDoesNotThrow(()-> postService.like(postId, username));
        }

        @Test
        @DisplayName("좋아요 알람 게시글 존재X 실패")
        void 좋아요알람_게시글_존재X_실패() {
            //Given
            Integer postId = 1;
            String username = "username";
            PostEntity post = EntityFixture.getPost1();
            AlarmEntity likeAlarm = EntityFixture.getLikeAlarm();

            //When
            when(postRepository.findById(postId)).thenReturn(Optional.empty());
            when(memberRepository.findByName(username)).thenReturn(Optional.of(post.getMember()));
            when(likeRepository.findByMemberAndPost(mock(MemberEntity.class), mock(PostEntity.class))).thenReturn(Optional.empty());
            when(likeRepository.save(any(LikeEntity.class))).thenReturn(mock(LikeEntity.class));
            when(alarmRepository.save(likeAlarm)).thenReturn(likeAlarm);


            //Then
            assertThrows(SnsException.class, ()-> postService.like(postId, username));
        }
        @Test
        @DisplayName("좋아요 알람 유저존재X 실패")
        void 좋아요알람_유저존재X_실패() {
            //Given
            Integer postId = 1;
            String username = "username";
            PostEntity post = EntityFixture.getPost1();
            AlarmEntity likeAlarm = EntityFixture.getLikeAlarm();

            //When
            when(postRepository.findById(postId)).thenReturn(Optional.of(post));
            when(memberRepository.findByName(username)).thenReturn(Optional.empty());
            when(likeRepository.findByMemberAndPost(mock(MemberEntity.class), mock(PostEntity.class))).thenReturn(Optional.empty());
            when(likeRepository.save(any(LikeEntity.class))).thenReturn(mock(LikeEntity.class));
            when(alarmRepository.save(likeAlarm)).thenReturn(likeAlarm);


            //Then
            assertThrows(SnsException.class, ()-> postService.like(postId, username));
        }
    }
}