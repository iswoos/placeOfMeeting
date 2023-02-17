package com.example.placemeeting.controller;

import com.example.placemeeting.dto.reqeustdto.MemberRequest;
import com.example.placemeeting.dto.responsedto.MemberResDto;
import com.example.placemeeting.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;



    @GetMapping("/posts")

    }

}
