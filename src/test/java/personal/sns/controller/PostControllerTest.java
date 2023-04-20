package personal.sns.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import personal.sns.controller.request.PostCreateRequest;
import personal.sns.controller.request.PostDeleteRequest;
import personal.sns.controller.request.PostModifyRequest;
import personal.sns.domain.MemberRole;
import personal.sns.domain.Post;
import personal.sns.domain.entity.LikeEntity;
import personal.sns.domain.entity.MemberEntity;
import personal.sns.domain.entity.PostEntity;
import personal.sns.exception.Errorcode;
import personal.sns.exception.SnsException;
import personal.sns.fixture.EntityFixture;
import personal.sns.service.MemberService;
import personal.sns.service.PostService;
import personal.sns.util.JwtTokenUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("게시글 컨트롤러")
@AutoConfigureMockMvc
class PostControllerTest {

    @MockBean
    private PostService postService;

    @MockBean
    private MemberService memberService;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Nested
    @DisplayName("게시글 생성 테스트 그룹")
    class createPostTests {
        @DisplayName("게시글 작성 권한O 성공")
        @WithMockUser
        @Test
        void 게시글_작성_권한O_성공() throws Exception {
            //Given
            String title = "title";
            String body = "body";

            //When

            //Then
            mvc.perform(post("/api/v1/post")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsBytes(new PostCreateRequest(title, body))))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @DisplayName("게시글 생성 권한X 실패")
        @WithAnonymousUser
        @Test
        void 게시글_작성_권한X_실패() throws Exception {
            //Given
            String title = "title";
            String body = "body";
            //When

            //Then
            mvc.perform(post("/api/v1/post")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsBytes(new PostCreateRequest(title, body))))
                    .andDo(print())
                    .andExpect(status().is(Errorcode.INVALID_TOKEN.getStatus().value()));
        }
    }

    @DisplayName("게시글 수정 테스트")
    @Nested
    public class modifyPost{
        @DisplayName("게시글 수정 권한O 성공")
        @WithMockUser(username = "username")
        @Test
        void 게시글_수정_권한O_성공() throws Exception {
            //Given
            String title = "title";
            String body = "body";
            Post post = Post.fromEntity(EntityFixture.getPost1(title, body));

            //When
            when(postService.modify(title, body, "username", 1)).thenReturn(post);

            //Then
            mvc.perform(put("/api/v1/post/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsBytes(new PostModifyRequest(title, body))))
                    .andDo(print())
                    .andExpect(status().isOk());
        }
        @DisplayName("게시글 수정 로그인 X 실패")
        @WithAnonymousUser()
        @Test
        void 게시글_수정_유저이름_존재X_실패() throws Exception {
            //Given
            String title = "title";
            String body = "body";
            //When
            doThrow(new SnsException(Errorcode.INVALID_PERMISSION)).when(postService).modify(eq(title), eq(body), eq("username"), eq(1));

            //Then
            mvc.perform(put("/api/v1/post/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsBytes(new PostModifyRequest(title, body))))
                    .andDo(print())
                    .andExpect(status().isUnauthorized());
        }
        @DisplayName("게시글 수정 게시글 존재X 실패")
        @WithMockUser(username = "username")
        @Test
        void 게시글_수정_게시글_존재X_실패() throws Exception {
            //Given
            String title = "title";
            String body = "body";

            //When
            doThrow(new SnsException(Errorcode.NOT_EXISTS_POST)).when(postService).modify(eq(title), eq(body), eq("username"), eq(1));

            //Then
            mvc.perform(put("/api/v1/post/{PostId}", 1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsBytes(new PostModifyRequest(title, body))))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }
        @DisplayName("게시글 수정 생성자 수정자 불일치 실패")
        @WithMockUser(username = "username")
        @Test
        void 게시글_수정_생성자_수정자_불일치_실패() throws Exception {
            //Given
            String title = "title";
            String body = "body";

            //When
            doThrow(new SnsException(Errorcode.INVALID_PERMISSION)).when(postService).modify(eq(title), eq(body), eq("username"), eq(1));

            //Then
            mvc.perform(put("/api/v1/post/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsBytes(new PostModifyRequest(title, body))))
                    .andDo(print())
                    .andExpect(status().isUnauthorized());
        }
        @DisplayName("게시글 수정 토큰만료 실패")
        @WithMockUser(username = "username")
        @Test
        void 게시글_수정_토큰만료_실패() throws Exception {
            //Given
            String title = "title";
            String body = "body";
            //When
            doThrow(new SnsException(Errorcode.INVALID_TOKEN)).when(postService).modify(eq(title), eq(body), eq("username"), eq(1));

            //Then
            mvc.perform(put("/api/v1/post/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer token")
                            .content(mapper.writeValueAsBytes(new PostCreateRequest(title, body))))
                    .andDo(print())
                    .andExpect(status().is(Errorcode.INVALID_TOKEN.getStatus().value()));
        }
    }

    @Nested
    @DisplayName("게시글 삭제 테스트")
    public class deletePost{
        @DisplayName("게시글 삭제 성공")
        @WithMockUser(username = "username")
        @Test
        void 게시글_삭제_성공() throws Exception {
            //Given
            String title = "title";
            String body = "body";
            Integer postId = 1;
            //When
            doNothing().when(postService).delete(eq(postId), eq("username"));

            //Then
            mvc.perform(delete("/api/v1/post/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer token")
                            .content(mapper.writeValueAsBytes(new PostDeleteRequest(postId))))
                    .andDo(print())
                    .andExpect(status().isOk());
        }
        @DisplayName("게시글 삭제 생성자 삭제자 불일치 실패")
        @WithMockUser(username = "username")
        @Test
        void 게시글_삭제_생성자_삭제자_불일치_실패() throws Exception {
            //Given
            String title = "title";
            String body = "body";
            Integer postId = 1;
            //When
            doThrow(new SnsException(Errorcode.INVALID_PERMISSION)).when(postService).delete(postId, "username");

            //Then
            mvc.perform(delete("/api/v1/post/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer token")
                            .content(mapper.writeValueAsBytes(new PostDeleteRequest(postId))))
                    .andDo(print())
                    .andExpect(status().isUnauthorized());
        }

        @DisplayName("게시글 삭제 토큰 만료 실패")
        @WithMockUser(username = "username")
        @Test
        void 게시글_삭제_토큰_만료_실패() throws Exception {
            //Given
            String title = "title";
            String body = "body";
            Integer postId = 1;
            //When
            doThrow(new SnsException(Errorcode.INVALID_TOKEN)).when(postService).delete(postId, "username");

            //Then
            mvc.perform(delete("/api/v1/post/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsBytes(new PostDeleteRequest(postId))))
                    .andDo(print())
                    .andExpect(status().isUnauthorized());
        }

        @DisplayName("게시글 삭제 존재 x 실패")
        @WithMockUser(username = "username")
        @Test
        void 게시글_삭제_존재_x_실패() throws Exception {
            //Given
            String title = "title";
            String body = "body";
            Integer postId = 1;
            //When
            doThrow(new SnsException(Errorcode.NOT_EXISTS_POST)).when(postService).delete(postId, "username");

            //Then
            mvc.perform(delete("/api/v1/post/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer token")
                            .content(mapper.writeValueAsBytes(new PostDeleteRequest(postId))))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

        @DisplayName("게시글 삭제 로그인 x 실패")
        @WithAnonymousUser
        @Test
        void 게시글_삭제_로그인_x_실패() throws Exception {
            //Given
            String title = "title";
            String body = "body";
            Integer postId = 1;
            //When
            doThrow(new SnsException(Errorcode.INVALID_PERMISSION)).when(postService).delete(postId, "username");

            //Then
            mvc.perform(delete("/api/v1/post/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsBytes(new PostDeleteRequest(postId))))
                    .andDo(print())
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    @DisplayName("피드 목록 테스트")
    class feedList{
        @DisplayName("게시글 목록 가져오기 성공")
        @WithAnonymousUser
        @Test
        void 게시글_목록_가져오기_성공() throws Exception {
            //Given

            //When
            when(postService.getList(any())).thenReturn(Page.empty());

            //Then
            mvc.perform(get("/api/v1/post/list")
                    .contentType("application/json")
            ).andExpect(status().isOk());
        }

        @DisplayName("게시글 목록 본인 포스트 가져오기 성공")
        @WithMockUser(username="username")
        @Test
        void 게시글_목록_본인_포스트_가져오기_성공() throws Exception {
            //Given
            Pageable pageable = mock(Pageable.class);

            //When
            when(postService.getMyPost(any(), any())).thenReturn(Page.empty());

            //Then
            mvc.perform(get("/api/v1/post/my")
                    .contentType("application/json")
            ).andExpect(status().isOk());
        }

        @DisplayName("게시글 목록 로그인 X 실패")
        @WithAnonymousUser
        @Test
        void 게시글_목록_본인_로그인X_실패() throws Exception {
            //Given
            Pageable pageable = mock(Pageable.class);

            //When
            when(postService.getMyPost(any(), any())).thenThrow(new SnsException(Errorcode.INVALID_TOKEN));

            //Then
            mvc.perform(get("/api/v1/post/my")
                    .contentType("application/json")
                    .header("Authoriaztion", "Baerer token")
            ).andExpect(status().is(Errorcode.INVALID_TOKEN.getStatus().value()));
        }

        @DisplayName("게시글 목록 토큰 만료 실패")
        @Test
        void 게시글_목록_본인_토큰만료_실패() throws Exception {
            //Given
            Pageable pageable = mock(Pageable.class);

            //When
            when(postService.getMyPost(any(), any())).thenThrow(new SnsException(Errorcode.INVALID_TOKEN));

            //Then
            mvc.perform(get("/api/v1/post/my")
                    .contentType("application/json")
                    .header("Authoriaztion", "Baerer token")
            ).andExpect(status().is(Errorcode.INVALID_TOKEN.getStatus().value()));
        }


    }

    @Nested
    @DisplayName("게시글 좋아요 테스트")
    class LikeTest{
        @DisplayName("게시글 좋아요 성공")
        @WithMockUser(username = "username")
        @Test
        void 게시글_좋아요_성공() throws Exception {
        //Given
            Integer postId = 1;

        //When
            doNothing().when(postService).like(anyInt(), any());

        //Then
            mvc.perform(post("/api/v1/post/{postId}/likes", postId)
                    .contentType("application/json")
            ).andExpect(status().isOk());
        }

        @DisplayName("게시글 좋아요 로그인 X 실패")
        @WithAnonymousUser
        @Test
        void 게시글_좋아요_로그인_X_실패() throws Exception {
            //Given
            Integer postId = 1;

            //When
            doThrow(new SnsException(Errorcode.INVALID_TOKEN)).when(postService).like(any(), any());

            //Then
            mvc.perform(post("/api/v1/post/{postId}/likes", postId)
                    .contentType("application/json")
            ).andExpect(status().is(Errorcode.INVALID_TOKEN.getStatus().value()));
        }

        @DisplayName("게시글 좋아요 이미 클릭 실패")
        @WithMockUser
        @Test
        void 게시글_좋아요_중복클릭_실패() throws Exception {
            //Given
            Integer postId = 1;

            //When
            doThrow(new SnsException(Errorcode.ALREADY_LIKE)).when(postService).like(any(), any());

            //Then
            mvc.perform(post("/api/v1/post/{postId}/likes", postId)
                    .contentType("application/json")
            ).andExpect(status().is(Errorcode.ALREADY_LIKE.getStatus().value()));
        }
        @DisplayName("게시글 좋아요 게시글 존재 X 실패")
        @WithMockUser
        @Test
        void 게시글_좋아요_게시글_존재_X_실패() throws Exception {
            //Given
            Integer postId = 1;

            //When
            doThrow(new SnsException(Errorcode.NOT_EXISTS_POST)).when(postService).like(any(), any());

            //Then
            mvc.perform(post("/api/v1/post/{postId}/likes", postId)
                    .contentType("application/json")
            ).andExpect(status().is(Errorcode.NOT_EXISTS_POST.getStatus().value()));
        }

    }

    @Nested
    @DisplayName("게시글 좋아요 카운트 테스트")
    class LikeCount {
        @Test
        @WithMockUser(username="username")
        @DisplayName("게시글 좋아요 카운트 성공")
        void 게시글_좋아요_카운트_성공() throws Exception {
            //Given
            Integer postId = 1;
            Integer count = 10;

            //When
            when(postService.likeCount(any())).thenReturn(count);

            //Then
            mvc.perform(get("/api/v1/post/{postId}/likecount", postId)
                    .contentType("application/json")
            ).andExpect(status().isOk());
        }

        @Test
        @WithMockUser(username = "username")
        @DisplayName("게시글 좋아요 카운트 게시글 존재X 실패")
        void 게시글_좋아요_카운트_게시글_존재X_실패() throws Exception {
            //Given
            Integer postId = 1;

            //When
            doThrow(new SnsException(Errorcode.NOT_EXISTS_POST)).when(postService).likeCount(any());

            //Then
            mvc.perform(get("/api/v1/post/{postId}/likecount", postId)
                    .contentType("application/json")
            ).andExpect(status().is(Errorcode.NOT_EXISTS_POST.getStatus().value()));
        }

        @Test
        @WithAnonymousUser
        @DisplayName("게시글 좋아요 카운트 로그인X 실패")
        void 게시글_좋아요_카운트_로그인X_실패() throws Exception {
            //Given
            Integer postId = 1;

            //When

            //Then
            mvc.perform(get("/api/v1/post/{postId}/likecount", postId)
                    .contentType("application/json")
            ).andExpect(status().is(Errorcode.INVALID_TOKEN.getStatus().value()));
        }
    }

}