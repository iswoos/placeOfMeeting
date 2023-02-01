package com.example.placemeeting.service;


import com.example.placemeeting.domain.Member;
import com.example.placemeeting.domain.RefreshToken;
import com.example.placemeeting.dto.reqeustdto.LoginRequest;
import com.example.placemeeting.dto.reqeustdto.MemberRequest;
import com.example.placemeeting.global.dto.GlobalResDto;
import com.example.placemeeting.jwt.dto.TokenDto;
import com.example.placemeeting.jwt.util.JwtUtil;
import com.example.placemeeting.repository.MemberRepository;
import com.example.placemeeting.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository accountRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public GlobalResDto signup(MemberRequest accountReqDto) {
        // userName 중복 검사
        if(accountRepository.findByuserName(accountReqDto.getUserName()).isPresent()){
            throw new RuntimeException("Overlap Check");
        }

        accountReqDto.setEncodePwd(passwordEncoder.encode(accountReqDto.getPassword()));
        Member account = new Member(accountReqDto);

        accountRepository.save(account);
        return new GlobalResDto("Success signup", HttpStatus.OK.value());
    }

    @Transactional
    public GlobalResDto login(LoginRequest loginReqDto, HttpServletResponse response) {

        Member account = accountRepository.findByuserName(loginReqDto.getUserName()).orElseThrow(
                () -> new RuntimeException("Not found Account")
        );

        if(!passwordEncoder.matches(loginReqDto.getPassword(), account.getPassword())) {
            throw new RuntimeException("Not matches Password");
        }

        TokenDto tokenDto = jwtUtil.createAllToken(loginReqDto.getUserName());

        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByuserName(loginReqDto.getUserName());

        if(refreshToken.isPresent()) {
            refreshTokenRepository.save(refreshToken.get().updateToken(tokenDto.getRefreshToken()));
        }else {
            RefreshToken newToken = new RefreshToken(tokenDto.getRefreshToken(), loginReqDto.getUserName());
            refreshTokenRepository.save(newToken);
        }

        setHeader(response, tokenDto);

        return new GlobalResDto("Success Login", HttpStatus.OK.value());

    }

    private void setHeader(HttpServletResponse response, TokenDto tokenDto) {
        response.addHeader(JwtUtil.ACCESS_TOKEN, tokenDto.getAccessToken());
        response.addHeader(JwtUtil.REFRESH_TOKEN, tokenDto.getRefreshToken());
    }
}
