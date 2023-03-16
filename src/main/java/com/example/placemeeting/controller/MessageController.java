package com.example.placemeeting.controller;

import com.example.placemeeting.domain.ChatMessage;
import com.example.placemeeting.dto.responsedto.ChatMessageResponse;
import com.example.placemeeting.dto.responsedto.ChatMessageResponse.ChatMessageResDto.MessageType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequiredArgsConstructor
public class MessageController { //채팅이 처리되는곳!

    private final SimpMessageSendingOperations sendingOperations; // @EnableWebSocketMessageBroker를 통해서 등록되는 Bean이다. Broker로 메시지를 전달한다.

    //클라이언트가 Send 할수 있는 경로
    // /pub/chat/message에 받으면 전처리 후에 converAndSend로 구독채널에 발행
    @MessageMapping("/chat/message")
    public void message(ChatMessage message) {
        System.out.println("채팅 시작!");
        LocalTime now = LocalTime.now();
        message.setSendTime(now.format(DateTimeFormatter.ofPattern("a HH시 mm분")));
        if (ChatMessage.MessageType.TALK.equals(message.getType())) {
            message.setMessage(message.getMessage() + " - " + message.getSendTime());
            sendingOperations.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
        }
    }

    // 입력별로 내가 할수있는 채팅 DB처리를 진행해보자
    
    @MessageMapping("/chat/enter")
    public void enter(ChatMessage message, SimpMessageHeaderAccessor headerAccessor) {
        LocalTime now = LocalTime.now();

        // stomp에 connect할 경우 자동으로 pub보내도록 설정하여 아래와 같이 socket session에 값 저장진행
        headerAccessor.getSessionAttributes().put("sender",message.getSender());
        headerAccessor.getSessionAttributes().put("roomId",message.getRoomId());

        message.setSendTime(now.format(DateTimeFormatter.ofPattern("a HH시 mm분")));
        if (ChatMessage.MessageType.ENTER.equals(message.getType())) {
            message.setMessage(message.getSender() + "님이 입장하였습니다" + " - " + message.getSendTime());
            sendingOperations.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
        }
    }

    // websocket Disconnect 감지하여 처리하는 eventListener
    @EventListener
    public void webSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        // socket 세션에 있던 sender 와 roomId 확인
        String sender = (String) headerAccessor.getSessionAttributes().get("sender");
        String roomId = (String) headerAccessor.getSessionAttributes().get("roomId");

        ChatMessageResponse.ChatMessageResDto chatMessageResDto = new ChatMessageResponse.ChatMessageResDto(
                MessageType.QUIT, roomId, sender, "", "");

        LocalTime now = LocalTime.now();
        chatMessageResDto.setSendTime(now.format(DateTimeFormatter.ofPattern("a HH시 mm분")));
        chatMessageResDto.setMessage(sender + "님이 퇴장하였습니다" + " - " + chatMessageResDto.getSendTime());

        sendingOperations.convertAndSend("/sub/chat/room/" + roomId, chatMessageResDto);
    }
}
