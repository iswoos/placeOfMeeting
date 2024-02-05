package com.example.placemeeting.controller;

import com.example.placemeeting.domain.ChatMessage;
import com.example.placemeeting.dto.reqeustdto.ChatMessageRequest;
import com.example.placemeeting.dto.responsedto.ChatMessageResponse;
import com.example.placemeeting.dto.responsedto.ChatMessageResponse.ChatMessageResDto.MessageType;
import com.example.placemeeting.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import nonapi.io.github.classgraph.json.JSONUtils;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequiredArgsConstructor
public class ChatMessageController { //채팅이 처리되는곳!

    private final SimpMessageSendingOperations sendingOperations; // @EnableWebSocketMessageBroker를 통해서 등록되는 Bean이다. Broker로 메시지를 전달한다.

    private final ChatMessageService chatMessageService;

    //클라이언트가 Send 할수 있는 경로
    // /pub/chat/message에 받으면 전처리 후에 converAndSend로 구독채널에 발행


    // 채팅방 내, 일반적인 정보교환 시 매핑됨
    // 1. 아래에 매핑되는 채팅 내용을 DB에 저장
    // 2. 저장요소 :  채팅 타입 / 채팅룸ID / 발신자 / 메시지 / 보낸시간
    @MessageMapping("/chat/message")
    public void message(ChatMessage message) {
        System.out.println("채팅 시작!");
        LocalTime now = LocalTime.now();
        message.setSendTime(now.format(DateTimeFormatter.ofPattern("HH:mm")));

        chatMessageService.createChatMessage(new ChatMessageRequest.ChatMessageCreate(message));

        if (ChatMessage.MessageType.TALK.equals(message.getType())) {
            message.setMessage(message.getMessage());
            sendingOperations.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
        }
    }

    // websocket connect시 (채팅방 입장) 자동실행
    @MessageMapping("/chat/enter")
    public void enter(ChatMessage message, SimpMessageHeaderAccessor headerAccessor) {
        LocalTime now = LocalTime.now();

        // stomp에 connect할 경우 자동으로 pub보내도록 설정하여 아래와 같이 socket session에 값 저장진행
        headerAccessor.getSessionAttributes().put("sender", message.getSender());
        headerAccessor.getSessionAttributes().put("roomId", message.getRoomId());
        message.setSendTime(now.format(DateTimeFormatter.ofPattern("HH:mm")));

        if (ChatMessage.MessageType.ENTER.equals(message.getType())) {
            message.setMessage(message.getSender() + "님이 입장하였습니다");
            sendingOperations.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
        }
    }

    // websocket Disconnect (채팅방 퇴장) 감지하여 처리하는 eventListener
    @EventListener
    public void webSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        // socket 세션에 있던 sender 와 roomId 확인
        String sender = (String) headerAccessor.getSessionAttributes().get("sender");
        String roomId = (String) headerAccessor.getSessionAttributes().get("roomId");

        ChatMessageResponse.ChatMessageResDto chatMessageResDto = new ChatMessageResponse.ChatMessageResDto(
                MessageType.QUIT, roomId, sender, "", "");

        LocalTime now = LocalTime.now();
        chatMessageResDto.setSendTime(now.format(DateTimeFormatter.ofPattern("HH:mm")));
        chatMessageResDto.setMessage(sender + "님이 퇴장하였습니다");

        sendingOperations.convertAndSend("/sub/chat/room/" + roomId, chatMessageResDto);
    }

    // 메시지를 불러올 때, 어떤 기준으로 주는 게 가장 좋을 지 생각해보자
    // 처음 입장하는 사용자에게 기존에 나눠진 대화를 보여주는 게 맞는 것일까?
    // 처음 입장했을 때 발생하는 이벤트 측에, 최근 입력한 채팅이 있을 시에만 해당 데이터 이후의 채팅만 불러일으키게 해볼지 고민해봐야겠다
//    @Transactional
//    public ChatMessageResponse.ChatMessageResDto chatMessageResDto() {
//
//    }
}


