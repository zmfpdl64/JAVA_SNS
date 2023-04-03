package personal.sns.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import personal.sns.domain.entity.MemberEntity;
import personal.sns.exception.exception.Errorcode;
import personal.sns.exception.exception.SnsException;
import personal.sns.fixture.EntityFixture;
import personal.sns.repository.MemberRepository;
import personal.sns.service.MemberService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@DisplayName("유저 서비스")
@SpringBootTest
class MemberServiceTest {

    @Autowired private MemberService memberService;

    @MockBean private MemberRepository memberRepository;


    @DisplayName("회원 가입 성공")
    @Test
    void 서비스_성공() {
        //Given
        String username = "username";
        String password = "password";
        MemberEntity member = MemberEntity.of(username, password);

        //When
        when(memberRepository.findByName(username)).thenReturn(Optional.empty());
        when(memberRepository.save(any())).thenReturn(EntityFixture.of(username, password));

        //Then
        assertDoesNotThrow(() -> memberService.join(username, password));
    }

    @DisplayName("회원 가입 아이디 중복 실패")
    @Test
    void 서비스_실패() {
        //Given
        String username = "username";
        String password = "password";
        MemberEntity member = EntityFixture.of(username, password);

        //When
        when(memberRepository.findByName(username)).thenReturn(Optional.of(member));


        //Then
        SnsException snsException = assertThrows(SnsException.class, () -> {
            memberService.join(username, password);
        });

        assertEquals(snsException.getErrorcode(), Errorcode.DUPLICATE_USERNAME);

    }

    @DisplayName("로그인 정상")
    @Test
    void 로그인_정상(){
        //Given
        String username = "username";
        String password = "password";
        MemberEntity member = EntityFixture.of(username, password);

        //When
        when(memberRepository.findByName(username)).thenReturn(Optional.of(member));

        //Then
        assertDoesNotThrow(() -> memberService.login(Login(username, password)));
    }

    @DisplayName("로그인 존재하지 않는 아이디 실패")
    @Test
    void 로그인_존재하지_않는_아이디_실패(){
        //Given
        String username = "username";
        String password = "password";
        MemberEntity member = EntityFixture.of(username, password);

        //When
        when(memberRepository.findByName(username)).thenReturn(Optional.empty());

        //Then
        SnsException exception = assertThrows(SnsException.class, () -> {
            memberService.login(Login(username, password));
        });

        assertEquals(exception.getErrorcode(), SnsException.NOTFOUNDNAME);
    }
    @DisplayName("로그인 일치하지 않는 비밀번호 실패")
    @Test
    void 로그인_일치하지_않는_비밀번호_실패(){
        //Given
        String username = "username";
        String password = "password";
        MemberEntity member = EntityFixture.of(username, password);

        //When
        when(memberRepository.findByName(username)).thenReturn(Optional.empty());

        //Then
        SnsException exception = assertThrows(SnsException.class, () -> {
            memberService.login(Login(username, password));
        });
        assertEquals(exception.getErrorcode(), SnsException.NOTMATCHPASSWORD);
    }

}