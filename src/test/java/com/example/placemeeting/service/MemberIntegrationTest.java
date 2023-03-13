package com.example.placemeeting.service;

import com.example.placemeeting.dto.reqeustdto.LoginRequest;
import com.example.placemeeting.dto.reqeustdto.MemberRequest;
import com.example.placemeeting.dto.responsedto.MemberResDto;
import com.example.placemeeting.exception.CustomCommonException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
/*
 아래 @SpringBootTest어노테이션이 없으면 스프링 컨테이너가 실행되지 않는다. (스프링에 의존하게 된다)
 그로 인해 하단에 있는 memberService를 스프링으로부터 주입받지 못한다 (실행 시 NPE가 발생한다)
 그런 이유로 SpringBootTest를 이용한 스프링 통합테스트의 실행은 단위테스트에 비해 느리다
 */
@SpringBootTest
// 테스트가 종료될 때 자동으로 해당 트랜잭션이 롤백되어 테스트 코드가 변경한 DB를 테스트 이전 상태로 돌려준다
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class MemberIntegrationTest {

    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("멤버 회원가입 테스트")
    public void 멤버_회원가입() {
        //given + when
        MemberResDto memberResDto = memberService.signup(new MemberRequest("lsw", "vugil", "유저네임", "010-4549-4736"));

        //then
        assertThat(memberResDto.getMsg()).isEqualTo("Success signup");
    }

    @Test
    @DisplayName("멤버 회원가입 아이디 중복체크 테스트")
    public void 멤버_회원가입_아이디_중복체크() {
        //given
        memberService.signup(new MemberRequest("lsw", "vugil", "유저네임", "010-4549-4736"));

        //when
        CustomCommonException customCommonException = assertThrows(CustomCommonException.class, () -> {
            memberService.signup(new MemberRequest("lsw", "vugil", "유저네임", "010-4549-4736"));
        });

        //then
        Assertions.assertEquals("중복된 아이디가 존재합니다.",customCommonException.getMessage());
    }

    @Test
    @DisplayName("멤버 회원가입 닉네임 중복체크 테스트")
    public void 멤버_회원가입_닉네임_중복체크() {
        //given
        memberService.signup(new MemberRequest("lsw", "lsw", "유저네임", "010-4549-4736"));

        //when
        CustomCommonException customCommonException = assertThrows(CustomCommonException.class, () -> {
            memberService.signup(new MemberRequest("lsww", "lsw", "유저네임", "010-4549-4736"));
        });

        //then
        Assertions.assertEquals("중복된 닉네임이 존재합니다.",customCommonException.getMessage());
    }

    @Test
    @DisplayName("멤버 로그인 테스트")
    public void 멤버_로그인() {
        //given
        memberService.signup(new MemberRequest("lsw", "lsw", "유저네임", "010-4549-4736"));
        MockHttpServletResponse mockResponse = new MockHttpServletResponse();

        //when
        MemberResDto memberResDto = memberService.login(new LoginRequest("lsw", "lsw",37.5427385,127.2135155), mockResponse);

        //then
        assertThat(memberResDto.getMsg()).isEqualTo("Success Login");
    }


}