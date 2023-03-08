package com.example.placemeeting.domain;

import com.example.placemeeting.dto.reqeustdto.MemberRequest;
import com.example.placemeeting.dto.responsedto.MemberResDto;
import com.example.placemeeting.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

/*
 아래 @SpringBootTest어노테이션이 없으면 스프링 컨테이너가 실행되지 않는다. (스프링에 의존하게 된다)
 그로 인해 하단에 있는 memberService를 스프링으로부터 주입받지 못한다 (실행 시 NPE가 발생한다)
 그런 이유로 SpringBootTest를 이용한 스프링 통합테스트의 실행은 단위테스트에 비해 느리다
 */
@SpringBootTest
// 테스트가 종료될 때 자동으로 해당 트랜잭션이 롤백되어 테스트 코드가 변경한 DB를 테스트 이전 상태로 돌려준다
@Transactional
class MemberTest {

    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("멤버 회원가입 테스트")
    public void 멤버_회원가입() {
        //given + when
        MemberResDto memberResDto = memberService.signup(new MemberRequest("a", "vugil", "아이다", "010-4549-4736"));

        //then
        assertThat(memberResDto.getMsg()).isEqualTo("Success signup");


    }

    @Test
    @DisplayName("진행종료 테스트")
    public void 멤버_종료테스트() {
        System.out.println("종료 테스트야");

    }

}