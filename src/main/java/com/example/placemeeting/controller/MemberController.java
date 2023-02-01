package com.example.placemeeting.controller;

import com.example.placemeeting.dto.reqeustdto.LoginRequest;
import com.example.placemeeting.dto.reqeustdto.MemberRequest;
import com.example.placemeeting.global.dto.MemberResDto;
import com.example.placemeeting.jwt.util.JwtUtil;
import com.example.placemeeting.security.user.UserDetailsImpl;
import com.example.placemeeting.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
    public MemberResDto issuedToken(@AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletResponse response){
        response.addHeader(JwtUtil.ACCESS_TOKEN, jwtUtil.createToken(userDetails.getAccount().getUserId(), "Access"));
        return new MemberResDto("Success IssuedToken", HttpStatus.OK.value());
    }

}
