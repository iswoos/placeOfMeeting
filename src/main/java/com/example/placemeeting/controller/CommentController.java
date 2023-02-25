package com.example.placemeeting.controller;

import com.example.placemeeting.dto.reqeustdto.PostRequest;
import com.example.placemeeting.global.dto.ResponseDto;
import com.example.placemeeting.security.user.UserDetailsImpl;
import com.example.placemeeting.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 게시물 댓글달기
    @PostMapping("/posts/{postId}/comments")
    public ResponseDto<String> createComment(@PathVariable Long postId, @RequestBody PostRequest.CommentCreate commentCreate, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseDto.success(commentService.createComment(postId, commentCreate, userDetails.getAccount()));
    }

}
