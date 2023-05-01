package personal.sns.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import personal.sns.domain.Member;
import personal.sns.domain.entity.MemberEntity;
import personal.sns.exception.Errorcode;
import personal.sns.exception.SnsException;
import personal.sns.fixture.EntityFixture;
import personal.sns.repository.AlarmEntityRepository;
import personal.sns.repository.MemberCacheRepository;
import personal.sns.repository.MemberRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


@DisplayName("유저 서비스")
@ActiveProfiles("test")
@SpringBootTest
class MemberServiceTest {

    @Autowired private MemberService memberService;

    @MockBean private MemberRepository memberRepository;
    @MockBean private MemberCacheRepository cacheRepository;
    @MockBean private AlarmEntityRepository alarmRepository;


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
        when(cacheRepository.getMember(username)).thenReturn(Optional.empty());
        when(memberRepository.findByName(username)).thenReturn(Optional.of(member));
        doNothing().when(cacheRepository).setMember(Member.fromEntity(member));

        //Then
        assertDoesNotThrow(() -> memberService.login(username, password));
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
            memberService.login(username, password);
        });

        assertEquals(exception.getErrorcode(), Errorcode.NOT_EXISTS_USERNAME);
    }
    @DisplayName("로그인 일치하지 않는 비밀번호 실패")
    @Test
    void 로그인_일치하지_않는_비밀번호_실패(){
        //Given
        String username = "username";
        String password = "password";
        String wrong_password = "wrong";
        MemberEntity member = EntityFixture.of(username, password);

        //When
        when(memberRepository.findByName(username)).thenReturn(Optional.of(member));

        //Then
        SnsException exception = assertThrows(SnsException.class, () ->
            memberService.login(username, wrong_password)
        );
        assertEquals(exception.getErrorcode(), Errorcode.NOT_MATCH_PASSWORD);
    }

    @Nested
    @DisplayName("내알림 테스트")
    class MyAlarmList{
        @Test
        @DisplayName("내알림 목록조회 성공")
        void 내알림_목록조회_성공(){
            //Given
            String username = "username";
            MemberEntity member = EntityFixture.of();

            //When
            when(memberRepository.findByName(username)).thenReturn(Optional.of(member));
            when(alarmRepository.findAllByMemberId(eq(1), any(Pageable.class))).thenReturn(Page.empty());

            //Then
            assertDoesNotThrow(() -> memberService.myAlarmList(username, PageRequest.of(0, 10)));
        }

        @Test
        @DisplayName("내알림 목록조회 유저존재X_실패")
        void 내알림_목록조회_유저존재X_실패(){
            //Given
            String username = "username";
            MemberEntity member = EntityFixture.of();

            //When
            when(memberRepository.findByName(username)).thenReturn(Optional.empty());
            when(alarmRepository.findAllByMemberId(eq(member.getId()), any(Pageable.class))).thenReturn(Page.empty());

            //Then
            assertThrows(SnsException.class ,() -> memberService.myAlarmList(eq(username), PageRequest.of(0,10)));
        }
    }

}