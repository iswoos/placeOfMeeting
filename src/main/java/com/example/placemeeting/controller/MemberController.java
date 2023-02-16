package com.example.placemeeting.controller;

import com.example.placemeeting.dto.reqeustdto.LoginRequest;
import com.example.placemeeting.dto.reqeustdto.MemberRequest;
import com.example.placemeeting.global.dto.MemberResDto;
import com.example.placemeeting.global.dto.ResponseDto;
import com.example.placemeeting.jwt.util.JwtUtil;
import com.example.placemeeting.security.user.UserDetailsImpl;
import com.example.placemeeting.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final JwtUtil jwtUtil;
    private final MemberService memberService;

    @PostMapping("/members/signup")
    public MemberResDto signup(@RequestBody @Valid MemberRequest memberReqDto) {
        return memberService.signup(memberReqDto);
    }

    @PostMapping("/members/login")
    public MemberResDto login(@RequestBody @Valid LoginRequest loginReqDto, HttpServletResponse response) {
        return memberService.login(loginReqDto, response);
    }

    @GetMapping("/issue/token")
    public ResponseDto<String> issuedToken(HttpServletRequest request, HttpServletResponse response) {
        return ResponseDto.success(memberService.issueToken(request,response));
    }

    @GetMapping("/members/location")
    public String getLocation(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return memberService.getLocation(userDetails.getAccount());
    }
}
