package com.example.placemeeting.controller.view;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class UIController {

    // 해당 컨트롤러를 통해 페이지를 출력한 것을 조금 더 다른 방식으로 개선할 수 있지않겠는가? 라는 피드백을 주셨음.
    // 이 출력과정을 어떻게 바꿔볼지 공부가 필요함...!
    // 모든 확장자 조회
    @GetMapping("/members/login")
    public String accountsLoginForm() {
        return "accounts/accountLoginForm";
    }

    @GetMapping("/members/signup")
    public String register() {
        return "accounts/accountRegisterForm";
    }

    @GetMapping("/admin/login")
    public String adminLoginForm() {
        return "accounts/adminLoginForm";
    }

    @GetMapping("/pom")
    public String mainPage() {
        return "main/home";
    }

    @GetMapping("/post/talk")
    public String talk() {
        return "postboard/talk";
    }

    @GetMapping("/post/lodging")
    public String lodging() {
        return "postboard/lodging";
    }

    @GetMapping("/post/food")
    public String food() {
        return "postboard/food";
    }

    @GetMapping("/post/hotplace")
    public String hotplace() {
        return "postboard/hotplace";
    }

    @GetMapping("/post/traffic")
    public String traffic() {
        return "postboard/traffic";
    }

    @GetMapping("/posts/view/{postId}")
    public String detailPostPage(@PathVariable Long postId, Model model) {
        model.addAttribute("postId", postId);
        return "postboard/detailPost";
    }

    @GetMapping("/posts/create/{postType}")
    public String createPost(@PathVariable String postType, Model model) {
        model.addAttribute("postType", postType);
        return "postboard/createPost";
    }

    // 채팅방 입장 화면
    @GetMapping("chat/room/enter/{roomId}") //채팅방 입장
    public String roomDetail(@PathVariable Long roomId, Model model) {
        model.addAttribute("roomId", roomId);
        return "chat/roomdetail";
    }
}