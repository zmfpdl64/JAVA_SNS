package personal.sns.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import personal.sns.domain.entity.MemberEntity;
import personal.sns.domain.exception.Errorcode;
import personal.sns.domain.exception.SnsException;
import personal.sns.repository.MemberRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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

        //When
        when(memberRepository.findByName(username)).thenReturn(Optional.empty());
        when(memberRepository.save(MemberEntity.of(username, password))).thenReturn(Optional.of());

        //Then
        assertDoesNotThrow(() -> memberService.join(username, password));
    }



}