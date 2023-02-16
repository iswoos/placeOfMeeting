package com.example.placemeeting.service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${naver.api.client-id}")
    private String clientId;

    @Value("${naver.api.client-secret}")
    private String clientSecret;

    private static final String NAVER_REVERSE_GEOCODING_API_URL = "https://naveropenapi.apigw.ntruss.com/map-reversegeocode/v2/gc?coords={lng},{lat}&output=json";

    @Value("${openweather.api.key}")
    private String weatherKey;

    private static final String WEATHER_API_URL = "https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API key}&lang=kr&units=metric";

    @Transactional
    public MemberResDto signup(MemberRequest memberReqDto) {
        // userId 중복 검사
        if(memberRepository.findByUserId(memberReqDto.getUserId()).isPresent()){
            throw new CustomCommonException(ErrorCode.DUPLICATE_ID);
        }

        // username 중복 검사
        if(memberRepository.findByUserName(memberReqDto.getUserName()).isPresent()){
            throw new CustomCommonException(ErrorCode.DUPLICATE_USERNAME);
        }

        memberReqDto.setEncodePwd(passwordEncoder.encode(memberReqDto.getPassword()));
        Member member = new Member(memberReqDto);

        memberRepository.save(member);
        return new MemberResDto("Success signup", HttpStatus.OK.value());
    }

    @Transactional
    public MemberResDto login(LoginRequest loginReqDto, HttpServletResponse response) {

        Member account = memberRepository.findByUserId(loginReqDto.getUserId()).orElseThrow(
                () -> new CustomCommonException(ErrorCode.USER_NOT_FOUND)
        );

        if(!passwordEncoder.matches(loginReqDto.getPassword(), account.getPassword())) {
            throw new CustomCommonException(ErrorCode.NOT_EQUAL_PASSWORD);
        }

        account.GeolocationSet(loginReqDto.getLatitude(), loginReqDto.getLongitude());

        String lng = loginReqDto.getLongitude().toString();
        String lat = loginReqDto.getLatitude().toString();

        // NAVER Reverse Geocoding API 호출
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-NCP-APIGW-API-KEY-ID", clientId);
        headers.set("X-NCP-APIGW-API-KEY", clientSecret);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                NAVER_REVERSE_GEOCODING_API_URL, HttpMethod.GET, requestEntity, String.class, lng, lat);
        // return responseEntity.getBody();
        String responseBody = responseEntity.getBody();
        // JSON 객체로 변환
        JSONObject jsonObject = new JSONObject(responseBody);

        // 필요한 데이터 추출
        JSONObject area2 = jsonObject.getJSONArray("results")
                .getJSONObject(0)
                .getJSONObject("region")
                .getJSONObject("area2");
        String cityName = area2.getString("name");

        account.localSet(cityName);

        TokenDto tokenDto = jwtUtil.createAllToken(loginReqDto.getUserId());

        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByUserId(loginReqDto.getUserId());

        if(refreshToken.isPresent()) {
            refreshTokenRepository.save(refreshToken.get().updateToken(tokenDto.getRefresh_Token()));
        }else {
            RefreshToken newToken = new RefreshToken(tokenDto.getRefresh_Token(), loginReqDto.getUserId());
            refreshTokenRepository.save(newToken);
        }

        setHeader(response, tokenDto);

        return new MemberResDto("Success Login", HttpStatus.OK.value());

    }

    public String issueToken(HttpServletRequest request, HttpServletResponse response){
        String refreshToken = jwtUtil.getHeaderToken(request, "Refresh");
        if(!jwtUtil.refreshTokenValidation(refreshToken)){
            throw new CustomCommonException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        response.addHeader(JwtUtil.ACCESS_TOKEN, jwtUtil.createToken(jwtUtil.getUserId(refreshToken), "Access"));
        return "Success IssuedToken";
    }

    private void setHeader(HttpServletResponse response, TokenDto tokenDto) {
        response.addHeader(JwtUtil.ACCESS_TOKEN, tokenDto.getAuthorization());
        response.addHeader(JwtUtil.REFRESH_TOKEN, tokenDto.getRefresh_Token());
    }


    public Map<String, Object> getLocation(Member member) throws JSONException{

        String lng = member.getLongitude().toString();
        String lat = member.getLatitude().toString();

        String cityName = member.getCityName();

        // OpenWeather API 호출
        RestTemplate restTemplate2 = new RestTemplate();
        HttpHeaders headers2 = new HttpHeaders();
        headers2.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity2 = new HttpEntity<>(headers2);
        ResponseEntity<String> responseEntity2 = restTemplate2.exchange(
                WEATHER_API_URL, HttpMethod.GET, requestEntity2, String.class, lat, lng, weatherKey);
        String responseBody2 = responseEntity2.getBody();
        JSONObject weatherObject = new JSONObject(responseBody2);
        JSONArray weatherArray = weatherObject.getJSONArray("weather");
        String weatherStatus = weatherArray.getJSONObject(0).getString("description");
        JSONObject mainObject = weatherObject.getJSONObject("main");
        double temperature = mainObject.getInt("temp");

        // 결과 반환
        Map<String, Object> result = new HashMap<>();
        result.put("cityName", cityName);
        result.put("weatherStatus", weatherStatus);
        result.put("temperature", temperature);

        return result;
    }
}
