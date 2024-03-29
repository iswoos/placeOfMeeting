//package com.example.placemeeting.service;
//
//import com.example.placemeeting.domain.Member;
//import com.example.placemeeting.dto.reqeustdto.LoginRequest;
//import com.example.placemeeting.dto.reqeustdto.MemberRequest;
//import com.example.placemeeting.dto.reqeustdto.PostRequest;
//import com.example.placemeeting.dto.responsedto.MemberResDto;
//import com.example.placemeeting.dto.responsedto.PostResponse;
//import com.example.placemeeting.repository.MemberRepository;
//import com.example.placemeeting.repository.PostRepository;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.mock.web.MockHttpServletResponse;
//import org.springframework.test.context.jdbc.Sql;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
//
//@SpringBootTest
//@Transactional
//// 통합 테스트 진행 시, 1번 생성된 Application Context를 테스트간에 공유함으로써 전체 테스트가 실패하고 있었음
//// 해당 현상해결을 위해 테스트케이스 수행하기 전, 수행한 후에 Application Context를 재생성하는 @DirtiesContext를 활용함
//// 하지만 context재생성으로 인해 테스트 소요시간이 매우 길다.
//// 추가 발생현상을 해결하기 위한 방법을 모색해보자
////@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
//
//// @sql 활용하여 테스트 메서드가 실행되기 전마다 테이블을 TRUNCATE하는 방식으로 변경하였음!
//// 해당 방식으로 인해 테스트 메서드마다 ApplicationContext가 새로 load되지 않도록 가능하게 하였음
//// truncate.sql 파일은 꼭 resources 폴더 내에 있어야함
//@Sql(scripts = {"classpath:sql/truncate.sql"}, executionPhase = BEFORE_TEST_METHOD)
//public class PostIntegrationTest {
//
//    @Autowired
//    private MemberService memberService;
//    @Autowired
//    private PostService postService;
//
//    @Autowired
//    private PostRepository postRepository;
//
//    @Autowired
//    private MemberRepository memberRepository;
//
//
//    @Test
//    @DisplayName("게시글 등록 테스트")
//    public void 게시글_등록() {
//        //given
//        memberService.signup(new MemberRequest("lsw", "lsw", "유저네임", "010-4549-4736"));
//        MockHttpServletResponse mockResponse = new MockHttpServletResponse();
//        MemberResDto login = memberService.login(new LoginRequest("lsw", "lsw", 37.5427385, 127.2135155), mockResponse);
//
//        //when
//        String response = postService.createPost(new PostRequest.PostCreate("제목", "글", "TALK"), memberRepository.findByUserId("lsw").orElseThrow());
//
//        //then
//        Assertions.assertThat(response).isEqualTo("게시물 등록완료");
//    }
//
//    @Test
//    @DisplayName("게시글 수정 테스트")
//    public void 게시글_수정() {
//        //given
//        memberService.signup(new MemberRequest("lsw", "lsw", "유저네임", "010-4549-4736"));
//        MockHttpServletResponse mockResponse = new MockHttpServletResponse();
//        MemberResDto login = memberService.login(new LoginRequest("lsw", "lsw", 37.5427385, 127.2135155), mockResponse);
//        postService.createPost(new PostRequest.PostCreate("제목", "글", "TALK"), memberRepository.findByUserId("lsw").orElseThrow());
//
//        //when
//        String response = postService.modifyPost(1L,new PostRequest.PostModify("수정 제목","수정 글"), memberRepository.findByUserId("lsw").orElseThrow());
//
//        //then
//        Assertions.assertThat(response).isEqualTo("게시물 수정완료");
//    }
//
//    @Test
//    @DisplayName("게시글 단건조회 테스트")
//    public void 게시글_단건조회() {
//        //given
//        memberService.signup(new MemberRequest("lsw", "lsw", "유저네임", "010-4549-4736"));
//        MockHttpServletResponse mockResponse = new MockHttpServletResponse();
//        MemberResDto login = memberService.login(new LoginRequest("lsw", "lsw", 37.5427385, 127.2135155), mockResponse);
//        Member member = memberRepository.findByUserId("lsw").orElseThrow();
//        postService.createPost(new PostRequest.PostCreate("제목", "글", "TALK"), member);
//
//        //when
//        PostResponse.PostDetailResDto post = postService.getPost(1L);
//
//        //then
//        Assertions.assertThat(post.getTitle()).isEqualTo("제목");
//        Assertions.assertThat(post.getContext()).isEqualTo("글");
//        Assertions.assertThat(post.getUserName()).isEqualTo("유저네임");
//        Assertions.assertThat(post.getCommentNum()).isEqualTo(0);
//        Assertions.assertThat(post.getLikeNum()).isEqualTo(0);
//        Assertions.assertThat(post.getCityName()).isEqualTo("하남시");
//    }
//
//    @Test
//    @DisplayName("게시글 전체조회 테스트")
//    public void 게시글_전체조회() {
//        //given
//        memberService.signup(new MemberRequest("lsw", "lsw", "유저네임", "010-4549-4736"));
//        MockHttpServletResponse mockResponse = new MockHttpServletResponse();
//        MemberResDto login = memberService.login(new LoginRequest("lsw", "lsw", 37.5427385, 127.2135155), mockResponse);
//        Member member = memberRepository.findByUserId("lsw").orElseThrow();
//        postService.createPost(new PostRequest.PostCreate("제목1", "글", "TALK"), member);
//        postService.createPost(new PostRequest.PostCreate("제목2", "글", "FOOD"), member);
//        postService.createPost(new PostRequest.PostCreate("제목3", "글", "FOOD"), member);
//        postService.createPost(new PostRequest.PostCreate("제목4", "글", "HOTPLACE"), member);
//
//        //when
//        List<PostResponse.PostMainResDto> postList = postService.getPosts("FOOD", member);
//
//        //then
//        Assertions.assertThat(postList.size()).isEqualTo(2);
//        Assertions.assertThat(postList.get(0).getTitle()).isEqualTo("제목3");
//    }
//
//    @Test
//    @DisplayName("게시글 삭제 테스트")
//    public void 게시글_삭제() {
//        //given
//        memberService.signup(new MemberRequest("lsw", "lsw", "유저네임", "010-4549-4736"));
//        MockHttpServletResponse mockResponse = new MockHttpServletResponse();
//        MemberResDto login = memberService.login(new LoginRequest("lsw", "lsw", 37.5427385, 127.2135155), mockResponse);
//        Member member = memberRepository.findByUserId("lsw").orElseThrow();
//        postService.createPost(new PostRequest.PostCreate("제목", "글", "TALK"), member);
//
//        //when
//        String response = postService.deletePost(1L, member);
//
//        //then
//        Assertions.assertThat(response).isEqualTo("게시물 삭제완료");
//    }
//
//    @Test
//    @DisplayName("게시글 좋아요 테스트")
//    public void 게시글_좋아요() {
//        //given
//        memberService.signup(new MemberRequest("lsw", "lsw", "유저네임", "010-4549-4736"));
//        MockHttpServletResponse mockResponse = new MockHttpServletResponse();
//        MemberResDto login = memberService.login(new LoginRequest("lsw", "lsw", 37.5427385, 127.2135155), mockResponse);
//        Member member = memberRepository.findByUserId("lsw").orElseThrow();
//        postService.createPost(new PostRequest.PostCreate("제목", "글", "TALK"), member);
//
//        //when
//        String response = postService.likePost(1L, member);
//
//        //then
//        Assertions.assertThat(response).isEqualTo("좋아요!");
//    }
//
//    @Test
//    @DisplayName("게시글 좋아요취소 테스트")
//    public void 게시글_좋아요취소() {
//        //given
//        memberService.signup(new MemberRequest("lsw", "lsw", "유저네임", "010-4549-4736"));
//        MockHttpServletResponse mockResponse = new MockHttpServletResponse();
//        MemberResDto login = memberService.login(new LoginRequest("lsw", "lsw", 37.5427385, 127.2135155), mockResponse);
//        Member member = memberRepository.findByUserId("lsw").orElseThrow();
//        postService.createPost(new PostRequest.PostCreate("제목", "글", "TALK"), member);
//
//        //when
//        postService.likePost(1L, member);
//        String response = postService.likePost(1L, member);
//
//        //then
//        Assertions.assertThat(response).isEqualTo("좋아요 취소!");
//    }
//}
