package com.example.placemeeting.service;


import com.example.placemeeting.domain.Member;
import com.example.placemeeting.domain.RefreshToken;
import com.example.placemeeting.dto.reqeustdto.LoginRequest;
import com.example.placemeeting.dto.reqeustdto.MemberRequest;
import com.example.placemeeting.exception.CustomCommonException;
import com.example.placemeeting.exception.ErrorCode;
import com.example.placemeeting.global.dto.MemberResDto;
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
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public MemberResDto signup(MemberRequest memberReqDto) {
        // userId 중복 검사
        if(memberRepository.findByuserId(memberReqDto.getUserId()).isPresent()){
            throw new CustomCommonException(ErrorCode.DUPLICATE_ID);
        }

        // username 중복 검사
        if(memberRepository.findByuserName(memberReqDto.getUserName()).isPresent()){
            throw new CustomCommonException(ErrorCode.DUPLICATE_USERNAME);
        }

        memberReqDto.setEncodePwd(passwordEncoder.encode(memberReqDto.getPassword()));
        Member member = new Member(memberReqDto);

        memberRepository.save(member);
        return new MemberResDto("Success signup", HttpStatus.OK.value());
    }

    @Transactional
    public MemberResDto login(LoginRequest loginReqDto, HttpServletResponse response) {

        Member account = memberRepository.findByuserId(loginReqDto.getUserId()).orElseThrow(
                () -> new CustomCommonException(ErrorCode.USER_NOT_FOUND)
        );

        if(!passwordEncoder.matches(loginReqDto.getPassword(), account.getPassword())) {
            throw new CustomCommonException(ErrorCode.NOT_EQUAL_PASSWORD);
        }

        account.GeolocationSet(loginReqDto.getLatitude(), loginReqDto.getLongitude());

        TokenDto tokenDto = jwtUtil.createAllToken(loginReqDto.getUserId());

        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByuserId(loginReqDto.getUserId());

        if(refreshToken.isPresent()) {
            refreshTokenRepository.save(refreshToken.get().updateToken(tokenDto.getRefreshToken()));
        }else {
            RefreshToken newToken = new RefreshToken(tokenDto.getRefreshToken(), loginReqDto.getUserId());
            refreshTokenRepository.save(newToken);
        }

        setHeader(response, tokenDto);

        return new MemberResDto("Success Login", HttpStatus.OK.value());

    }

    private void setHeader(HttpServletResponse response, TokenDto tokenDto) {
        response.addHeader(JwtUtil.ACCESS_TOKEN, tokenDto.getAccessToken());
        response.addHeader(JwtUtil.REFRESH_TOKEN, tokenDto.getRefreshToken());
    }
}
