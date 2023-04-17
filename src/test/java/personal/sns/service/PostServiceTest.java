package personal.sns.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import personal.sns.domain.entity.MemberEntity;
import personal.sns.domain.entity.PostEntity;
import personal.sns.exception.Errorcode;
import personal.sns.exception.SnsException;
import personal.sns.fixture.EntityFixture;
import personal.sns.repository.MemberRepository;
import personal.sns.repository.PostEntityRepository;

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
        when(postRepository.save(post)).thenReturn(post);

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