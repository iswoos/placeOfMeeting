package com.example.placemeeting.controller;


import com.example.placemeeting.domain.ChatRoom;
import com.example.placemeeting.dto.responsedto.ChatRoomResponse.ChatRoomResDto;
import com.example.placemeeting.global.dto.ResponseDto;
import com.example.placemeeting.security.user.UserDetailsImpl;
import com.example.placemeeting.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatRoomController {
    private final ChatService chatService;

    // 모든 채팅방 목록 반환
    @GetMapping("/rooms")
    public ResponseDto<List<ChatRoomResDto>> room(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseDto.success(chatService.findAllRoom(userDetails.getAccount()));
    }

    // 채팅방 생성
    @PostMapping("/room") //채팅방 생성
    @ResponseBody
    public ResponseDto<ChatRoom> createRoom(@RequestParam("chatType") String chatType, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseDto.success(chatService.createRoom(chatType, userDetails.getAccount()));
    }

    // 채팅방 입장 화면
    @GetMapping("/room/enter/{roomId}") //채팅방 입장
    public String roomDetail(Model model, @PathVariable String roomId) {
        model.addAttribute("roomId", roomId);
        System.out.println("방입장");
        return "/chat/roomdetail";
    }

    // 특정 채팅방 조회
    @GetMapping("/room/{roomId}")
    @ResponseBody
    public ChatRoom roomInfo(@PathVariable String roomId) {
        return chatService.findById(roomId);
    }
}
