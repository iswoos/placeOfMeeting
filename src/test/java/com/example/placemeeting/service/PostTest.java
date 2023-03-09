package com.example.placemeeting.service;

import com.example.placemeeting.domain.Member;
import com.example.placemeeting.dto.reqeustdto.LoginRequest;
import com.example.placemeeting.dto.reqeustdto.MemberRequest;
import com.example.placemeeting.dto.reqeustdto.PostRequest;
import com.example.placemeeting.dto.responsedto.MemberResDto;
import com.example.placemeeting.repository.MemberRepository;
import com.example.placemeeting.repository.PostRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class PostTest {

    @Autowired
    private MemberService memberService;
    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("게시글 등록 테스트")
    public void 게시글_등록() {
        //given
        memberService.signup(new MemberRequest("lsw", "lsw", "유저네임", "010-4549-4736"));
        MockHttpServletResponse mockResponse = new MockHttpServletResponse();
        MemberResDto login = memberService.login(new LoginRequest("lsw", "lsw", 37.5427385, 127.2135155), mockResponse);

        //when
        String response = postService.createPost(new PostRequest.PostCreate("제목", "글", "TALK"), memberRepository.findByUserId("lsw").orElseThrow());

        //then
        Assertions.assertThat(response).isEqualTo("게시물 등록완료");
    }

    @Test
    @DisplayName("게시글 수정 테스트")
    public void 게시글_수정() {
        //given
        memberService.signup(new MemberRequest("lsww", "lsww", "유저네임s", "010-4549-4736"));
        MockHttpServletResponse mockResponse = new MockHttpServletResponse();
        MemberResDto login = memberService.login(new LoginRequest("lsw", "lsw", 37.5427385, 127.2135155), mockResponse);
        postService.createPost(new PostRequest.PostCreate("제목", "글", "TALK"), memberRepository.findByUserId("lsw").orElseThrow());

        //when
        String response = postService.modifyPost(1L,new PostRequest.PostModify("수정 제목","수정 글"), memberRepository.findByUserId("lsw").orElseThrow());

        //then
        Assertions.assertThat(response).isEqualTo("게시물 수정완료");
    }
}
