package personal.sns.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import personal.sns.controller.request.PostCreateRequest;
import personal.sns.controller.request.PostDeleteRequest;
import personal.sns.controller.request.PostModifyRequest;
import personal.sns.controller.response.PostModifyResponse;
import personal.sns.controller.response.PostResponse;
import personal.sns.controller.response.Response;
import personal.sns.domain.Post;
import personal.sns.service.MemberService;
import personal.sns.service.PostService;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/post")
public class PostController {

    private final PostService postService;
    private final MemberService memberService;

    @GetMapping("/list")
    public Response<Page<PostResponse>> getPostList(Pageable pageable, Authentication authentication) {
        Page<Post> lists = postService.getList(pageable);
        return Response.success();
    }

    @PostMapping
    public Response<Void> createPost(@RequestBody PostCreateRequest request, Authentication authentication){
        postService.create(request.getTitle(), request.getBody(), authentication.getName());
        return Response.success();
    }

    @PutMapping("/{postId}")
    public Response<PostModifyResponse> modifyPost(@RequestBody PostModifyRequest request, @PathVariable Integer postId, Authentication authentication){
        log.info("요청: 게시글 ID {}", postId);
        Post post = postService.modify(request.getTitle(), request.getBody(), authentication.getName(), postId);
        return Response.success(PostModifyResponse.fromPost(post));
    }

    @DeleteMapping("/{postId}")
    public Response<Void> deletePost(@RequestBody PostDeleteRequest request, @PathVariable Integer postId, Authentication authentication){
        postService.delete(postId, authentication.getName());

        return Response.success();
    }


}
