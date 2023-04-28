package personal.sns.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import personal.sns.controller.request.CommentCreateRequest;
import personal.sns.controller.request.PostCreateRequest;
import personal.sns.controller.request.PostDeleteRequest;
import personal.sns.controller.request.PostModifyRequest;
import personal.sns.controller.response.CommentResponse;
import personal.sns.controller.response.PostModifyResponse;
import personal.sns.controller.response.PostResponse;
import personal.sns.controller.response.Response;
import personal.sns.domain.Member;
import personal.sns.domain.Post;
import personal.sns.exception.Errorcode;
import personal.sns.exception.SnsException;
import personal.sns.service.MemberService;
import personal.sns.service.PostService;
import personal.sns.util.ClassUtils;

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
        return Response.success(lists.map(PostResponse::fromPost));
    }
    @GetMapping("/my")
    public Response<Page<PostResponse>> getMyPost(Pageable pageable, Authentication authentication) {
        Member member = getSafeCastInstance(authentication);
        Page<Post> lists = postService.getMyPost(pageable, member.getName());
        return Response.success(lists.map(PostResponse::fromPost));
    }

    @PostMapping
    public Response<Void> createPost(@RequestBody PostCreateRequest request, Authentication authentication){
        Member member = getSafeCastInstance(authentication);

        postService.create(request.getTitle(), request.getBody(), member.getName());
        return Response.success();
    }

    @PutMapping("/{postId}")
    public Response<PostModifyResponse> modifyPost(@RequestBody PostModifyRequest request, @PathVariable Integer postId, Authentication authentication){
        log.info("요청: 게시글 ID {}", postId);
        Member member = getSafeCastInstance(authentication);

        Post post = postService.modify(request.getTitle(), request.getBody(), member.getName(), postId);
        return Response.success(PostModifyResponse.fromPost(post));
    }

    @DeleteMapping("/{postId}")
    public Response<Void> deletePost(@RequestBody PostDeleteRequest request, @PathVariable Integer postId, Authentication authentication){
        Member member = getSafeCastInstance(authentication);

        postService.delete(postId, member.getName());

        return Response.success();
    }

    @PostMapping("/{postId}/likes")
    public Response<Void> like(@PathVariable("postId") Integer postId, Authentication authentication) {
        Member member = getSafeCastInstance(authentication);

        postService.like(postId, member.getName());
        return Response.success();
    }

    @GetMapping("/{postId}/likecount")
    public Response<Integer> likeCount(@PathVariable("postId") Integer postId, Authentication authentication) {
        return Response.success(postService.likeCount(postId));
    }

    @PostMapping("/{postId}/comments")
    public Response<Void> createComment(@PathVariable("postId") Integer postId,@RequestBody CommentCreateRequest request, Authentication authentication){
        log.info("info: {}", request.getComment());
        Member member = getSafeCastInstance(authentication);

        postService.createComment(request.getComment(), postId, member.getName());
        return Response.success();
    }

    @GetMapping("/{postId}/comments")
    public Response<Page<CommentResponse>> createComment(@PathVariable("postId") Integer postId, Authentication authentication, Pageable pageable){
        Member member = getSafeCastInstance(authentication);

        Page<CommentResponse> response = postService.getComments(postId, member.getName(), pageable).map(CommentResponse::fromComment);
        return Response.success(response);
    }

    @GetMapping("/mycomments")
    public Response<Page<CommentResponse>> getMyComments(Authentication authentication, Pageable pageable) {
        Member member = getSafeCastInstance(authentication);

        Page<CommentResponse> response = postService.getMyComments(member.getName(), pageable).map(CommentResponse::fromComment);
        return Response.success(response);
    }
    private static Member getSafeCastInstance(Authentication authentication) {
        return ClassUtils.getSafeCastInstance(authentication.getPrincipal(), Member.class).orElseThrow(
                () -> new SnsException(Errorcode.INTERNAL_SERVER_ERROR, String.format("멤버.class로 캐스팅하지 못했습니다."))
        );
    }

}
