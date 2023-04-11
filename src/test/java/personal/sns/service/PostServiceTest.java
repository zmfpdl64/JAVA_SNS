package personal.sns.service;

import net.bytebuddy.dynamic.DynamicType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import personal.sns.exception.SnsException;
import personal.sns.fixture.EntityFixture;
import personal.sns.repository.MemberRepository;
import personal.sns.repository.PostRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@DisplayName("게시글 서비스 테스트")
@SpringBootTest
@ActiveProfiles("test")
class PostServiceTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MemberRepository memberRepository;

    @MockBean
    private PostService postService;


    @DisplayName("게시글 권한 O 정상")
    @Test
    void 게시글_권한O_정상() {
        //given
        String title = "title";
        String body = "body";

        //when
        when(memberRepository.findByName("username")).thenReturn(Optional.of(EntityFixture.of()));

        //then //TODO: 유저이름 인증기능 구현 후 수정
        assertDoesNotThrow(() -> postService.create(title, body, "username"));
    }

    @DisplayName("게시글 권한 X 실패")
    @Test
    void 게시글_권한X_실패() {
        //given
        String title = "title";
        String body = "body";

        //when
        when(memberRepository.findByName("username")).thenReturn(Optional.empty());

        //then //TODO: 유저이름 인증기능 구현 후 수정
        assertThrows(SnsException.class, () ->
            postService.create(title, body, "username")
        );
    }
}