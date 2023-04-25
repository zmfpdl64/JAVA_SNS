package personal.sns.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.PrePersist;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import personal.sns.controller.request.MemberJoinRequest;
import personal.sns.controller.request.MemberLoginRequest;
import personal.sns.domain.Member;
import personal.sns.domain.entity.CommentEntity;
import personal.sns.domain.entity.MemberEntity;
import personal.sns.exception.Errorcode;
import personal.sns.exception.SnsException;
import personal.sns.fixture.EntityFixture;
import personal.sns.service.MemberService;
import personal.sns.service.PostService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("유저 컨트롤러 테스트")
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class MemberControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    MemberService memberService;
    @MockBean
    PostService postService;

    @Autowired
    ObjectMapper mapper;

    @DisplayName("회원가입 페이지 정상")
    @WithAnonymousUser
    @Test
    void 회원가입_페이지_정상 () throws Exception {
        //Given
        String username = "username";
        String password = "password";

        //When
        when(memberService.join(username, password)).thenReturn(mock(Member.class));

        mvc.perform(post("/api/v1/user/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(new MemberJoinRequest(username, password))))
                .andDo(print())
                .andExpect(status().isOk());
        //Then

    }
    @DisplayName("회원가입 아이디 존재 실패")
    @Test
    void 회원가입_아이디_존재_실패() throws Exception {
        //Given
        String username = "username";
        String password = "password";

        //When
        when(memberService.join(username, password)).thenThrow(new SnsException(Errorcode.DUPLICATE_USERNAME));

        mvc.perform(post("/api/v1/user/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(new MemberJoinRequest(username, password))))
                .andDo(print())
                .andExpect(status().is(Errorcode.DUPLICATE_USERNAME.getStatus().value()));
        //Then
    }

    @DisplayName("로그인 정상")
    @Test
    void 로그인_정상() throws Exception {
        //Given
        String username = "username";
        String password = "password";
        MemberEntity member = EntityFixture.of(username, password);

        //When
        when(memberService.login(username, password)).thenReturn("token");

        mvc.perform(post("/api/v1/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(new MemberLoginRequest(username, password)))
        ).andDo(print())
                .andExpect(status().isOk());
        //Then

    }

    @DisplayName("로그인 아이디 존재하지 않음 실패")
    @Test
    void 로그인_아이디_존재하지_않음_실패() throws Exception {
        //Given
        String username = "username";
        String password = "password";
        MemberEntity member = EntityFixture.of(username, password);

        //When
        when(memberService.login(username, password)).thenThrow(new SnsException(Errorcode.NOT_EXISTS_USERNAME));
        mvc.perform(post("/api/v1/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(new MemberLoginRequest(username, password)))
                ).andDo(print())
                .andExpect(status().is(Errorcode.NOT_EXISTS_USERNAME.getStatus().value()));
        //Then

    }

    @DisplayName("로그인 아이디 일치 패스워드 불일치 실패")
    @Test
    void 로그인_아이디_일치_패스워드_불일치_실패() throws Exception {
        //Given
        String username = "username";
        String password = "password";
        MemberEntity member = EntityFixture.of(username, password);

        //When
        when(memberService.login(username, password)).thenThrow(new SnsException(Errorcode.NOT_MATCH_PASSWORD));
        mvc.perform(post("/api/v1/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(new MemberLoginRequest(username, password)))
                ).andDo(print())
                .andExpect(status().is(Errorcode.NOT_MATCH_PASSWORD.getStatus().value()));

        //Then

    }



}