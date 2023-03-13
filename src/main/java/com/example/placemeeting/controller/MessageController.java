package com.example.placemeeting.controller;

import com.example.placemeeting.domain.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
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
    // /pub/chat/message 받으면
    @MessageMapping("/chat/message")
    public void enter(ChatMessage message) {
        System.out.println("채팅 시작!");
        LocalTime now = LocalTime.now();
        message.setSendTime(now.format(DateTimeFormatter.ofPattern("a HH시 mm분")));
        if (ChatMessage.MessageType.ENTER.equals(message.getType())) {
            message.setMessage(message.getSender() + "님이 입장하였습니다" + " - " + message.getSendTime());
        } else if (ChatMessage.MessageType.QUIT.equals(message.getType())) {
            message.setMessage(message.getSender() + "님이 퇴장하였습니다" + " - " + message.getSendTime());
        } else {
            message.setMessage(message.getMessage() + " - " + message.getSendTime());
        }
        sendingOperations.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }

    @EventListener
    public void webSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String roomId = (String) headerAccessor.getSessionAttributes().get("roomId");
        System.out.println("aaa");
        System.out.println(roomId);

        sendingOperations.convertAndSend("/sub/chat/room/" + roomId, 123);

//        message.setMessage(message.getSender() + "님이 퇴장하였습니다" + " - " + message.getSendTime());
    }
}
