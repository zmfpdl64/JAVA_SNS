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
import personal.sns.service.MemberService;

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
        Page<AlarmResponse> alarms = memberService.myAlarmList(authentication.getName(), pageable).map(AlarmResponse::fromAlarm);
        return Response.success(alarms);
    }

}
