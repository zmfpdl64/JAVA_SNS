package personal.sns.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import personal.sns.domain.entity.PostEntity;
import personal.sns.exception.SnsException;
import personal.sns.fixture.EntityFixture;
import personal.sns.repository.MemberRepository;
import personal.sns.repository.PostEntityRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


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


}