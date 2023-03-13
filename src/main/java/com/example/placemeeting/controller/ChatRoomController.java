package com.example.placemeeting.controller;


import com.example.placemeeting.domain.ChatRoom;
import com.example.placemeeting.dto.reqeustdto.ChatRoomRequest;
import com.example.placemeeting.dto.reqeustdto.ChatRoomRequest.ChatRoomCreate;
import com.example.placemeeting.dto.responsedto.ChatRoomResponse;
import com.example.placemeeting.dto.responsedto.ChatRoomResponse.ChatRoomResDto;
import com.example.placemeeting.global.dto.ResponseDto;
import com.example.placemeeting.security.user.UserDetailsImpl;
import com.example.placemeeting.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
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
    public ResponseDto<ChatRoom> createRoom(@RequestBody ChatRoomCreate chatRoomCreate, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseDto.success(chatService.createRoom(chatRoomCreate, userDetails.getAccount()));
    }

    // 특정 채팅방 조회
    @GetMapping("/room/{roomId}")
    public ResponseDto<ChatRoomResDto> roomInfo(@PathVariable Long roomId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseDto.success(chatService.findById(roomId, userDetails.getAccount()));
    }
}
