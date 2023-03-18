package com.example.placemeeting.service;

import com.example.placemeeting.domain.ChatMessage;
import com.example.placemeeting.domain.ChatRoom;
import com.example.placemeeting.domain.Member;
import com.example.placemeeting.dto.reqeustdto.ChatMessageRequest;
import com.example.placemeeting.dto.reqeustdto.ChatRoomRequest;
import com.example.placemeeting.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    //채팅메시지 생성 및 저장
    @Transactional
    public ChatMessageRequest.ChatMessageCreate createChatMessage(ChatMessageRequest.ChatMessageCreate chatMessageCreate) {
        ChatMessage chatMessage = new ChatMessage(chatMessageCreate);
        chatMessageRepository.save(chatMessage);
        return chatMessageCreate;
    }
}


