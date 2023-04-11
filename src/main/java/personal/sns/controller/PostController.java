package personal.sns.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import personal.sns.controller.request.PostRequest;
import personal.sns.controller.response.Response;
import personal.sns.service.MemberService;
import personal.sns.service.PostService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/post")
public class PostController {

    private final PostService postService;
    private final MemberService memberService;

    @PostMapping
    public Response<Void> createPost(@RequestBody PostRequest request, Authentication authentication) {
        //TODO: 유저이름 전달하기

        postService.create(request.getTitle(), request.getBody(), authentication.getName());

        return Response.success();
    }


}
