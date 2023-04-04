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


}