package personal.sns.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import personal.sns.controller.request.MemberJoinRequest;
import personal.sns.controller.response.MemberJoinResponse;
import personal.sns.controller.response.Response;
import personal.sns.service.MemberService;

@RequiredArgsConstructor
@RequestMapping("/api/user")
@RestController
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/join")
    public Response<MemberJoinResponse> join(@RequestBody MemberJoinRequest request) {
        return Response.success(MemberJoinResponse.fromMember(memberService.join(request.getUsername(), request.getPassword())));
    }

}
