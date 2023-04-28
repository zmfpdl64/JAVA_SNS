package personal.sns.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import personal.sns.controller.request.MemberJoinRequest;
import personal.sns.controller.response.AlarmResponse;
import personal.sns.controller.response.MemberJoinResponse;
import personal.sns.controller.response.MemberLoginResponse;
import personal.sns.controller.response.Response;
import personal.sns.domain.Member;
import personal.sns.exception.Errorcode;
import personal.sns.exception.SnsException;
import personal.sns.service.MemberService;
import personal.sns.util.ClassUtils;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@RestController
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/join")
    public Response<MemberJoinResponse> join(@RequestBody MemberJoinRequest request) {
        return Response.success(MemberJoinResponse.fromMember(memberService.join(request.getUsername(), request.getPassword())));
    }
    @PostMapping("/login")
    public Response<MemberLoginResponse> login(@RequestBody MemberJoinRequest request){
        String token = memberService.login(request.getUsername(), request.getPassword());

        return Response.success(new MemberLoginResponse(token));
    }
    @GetMapping("/myalarm")
    public Response<Page<AlarmResponse>> alarmList(Authentication authentication, Pageable pageable){
        Member member = getSafeCastInstance(authentication);
        Page<AlarmResponse> alarms = memberService.myAlarmList(member.getName(), pageable).map(AlarmResponse::fromAlarm);
        return Response.success(alarms);
    }

    private static Member getSafeCastInstance(Authentication authentication) {
        return ClassUtils.getSafeCastInstance(authentication.getPrincipal(), Member.class).orElseThrow(
                () -> new SnsException(Errorcode.INTERNAL_SERVER_ERROR, String.format("멤버.class로 캐스팅하지 못했습니다."))
        );
    }


}
