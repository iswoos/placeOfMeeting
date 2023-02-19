package com.example.placemeeting.controller;

import com.example.placemeeting.global.dto.ResponseDto;
import com.example.placemeeting.security.user.UserDetailsImpl;
import com.example.placemeeting.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;


    @GetMapping("/posts")
    public ResponseDto<List> getPosts(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseDto.success(postService.getPosts(userDetails.getAccount()));
    }

}
