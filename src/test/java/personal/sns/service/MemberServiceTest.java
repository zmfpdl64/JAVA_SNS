package personal.sns.service;

import jakarta.persistence.PrePersist;
import org.hibernate.cfg.Environment;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.TestPropertySources;
import personal.sns.domain.entity.MemberEntity;
import personal.sns.exception.exception.Errorcode;
import personal.sns.exception.exception.SnsException;
import personal.sns.fixture.EntityFixture;
import personal.sns.repository.MemberRepository;
import personal.sns.service.MemberService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@DisplayName("유저 서비스")
@ActiveProfiles("test")
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