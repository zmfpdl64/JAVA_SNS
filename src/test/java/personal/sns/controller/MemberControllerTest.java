package personal.sns.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import personal.sns.controller.request.MemberJoinRequest;
import personal.sns.domain.Member;
import personal.sns.exception.exception.Errorcode;
import personal.sns.exception.exception.SnsException;
import personal.sns.service.MemberService;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("유저 컨트롤러 테스트")
@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    MemberService memberService;

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

        mvc.perform(post("/api/user/join")
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
//        System.out.println(Errorcode.DUPLICATE_USERNAME.getStatus().value());

        mvc.perform(post("/api/user/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(new MemberJoinRequest(username, password))))
                .andDo(print())
                .andExpect(status().isConflict());
        //Then
    }


}