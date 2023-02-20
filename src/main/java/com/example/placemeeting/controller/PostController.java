package com.example.placemeeting.controller;

import com.example.placemeeting.dto.reqeustdto.PostRequest;
import com.example.placemeeting.dto.responsedto.PostResponse;
import com.example.placemeeting.dto.responsedto.PostResponse.PostDetailResDto;
import com.example.placemeeting.dto.responsedto.PostResponse.PostMainResDto;
import com.example.placemeeting.global.dto.ResponseDto;
import com.example.placemeeting.security.user.UserDetailsImpl;
import com.example.placemeeting.service.PostService;
import io.swagger.models.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;


    // 게시물 등록하기
    @PostMapping("/posts")
    public ResponseDto<String> createPost(@Valid @RequestBody PostRequest.PostCreate postCreate, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseDto.success(postService.createPost(postCreate, userDetails.getAccount()));
    }

    // 카테고리, 도시에 맞는 전체 게시물 조회
    @GetMapping("/posts")
    public ResponseDto<List<PostMainResDto>> getPosts(@RequestParam("postType") String postType, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseDto.success(postService.getPosts(postType, userDetails.getAccount()));
    }

    // 게시물 단건조회
    @GetMapping("/posts/{postId}")
    public ResponseDto<PostDetailResDto> getPost(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseDto.success(postService.getPost(postId, userDetails.getAccount()));
    }
}
