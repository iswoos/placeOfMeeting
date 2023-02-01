package com.example.placemeeting.controller;

import com.example.placemeeting.dto.reqeustdto.LoginRequest;
import com.example.placemeeting.dto.reqeustdto.MemberRequest;
import com.example.placemeeting.global.dto.GlobalResDto;
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
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {

    private final JwtUtil jwtUtil;
    private final MemberService accountService;

    @PostMapping("/members/signup")
    public GlobalResDto signup(@RequestBody @Valid MemberRequest accountReqDto) {
        return accountService.signup(accountReqDto);
    }

    @PostMapping("/members/login")
    public GlobalResDto login(@RequestBody @Valid LoginRequest loginReqDto, HttpServletResponse response) {
        return accountService.login(loginReqDto, response);
    }

    @GetMapping("/issue/token")
    public GlobalResDto issuedToken(@AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletResponse response){
        response.addHeader(JwtUtil.ACCESS_TOKEN, jwtUtil.createToken(userDetails.getAccount().getUserName(), "Access"));
        return new GlobalResDto("Success IssuedToken", HttpStatus.OK.value());
    }

}
