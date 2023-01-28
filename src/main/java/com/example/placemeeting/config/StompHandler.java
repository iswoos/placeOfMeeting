package com.example.placemeeting.config;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class StompHandler implements ChannelInterceptor {

    @Override
    public Message preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        StompCommand command = accessor.getCommand();
        if (command.compareTo(StompCommand.SUBSCRIBE) == 0) {
            String destination = accessor.getDestination();
            System.out.println("구독 주소 : " + destination);
            System.out.println(message);
        } else if (command.compareTo(StompCommand.CONNECT) == 0) {
            System.out.println("사용자 연결");
        } else if (command.compareTo(StompCommand.DISCONNECT) == 0) {
            System.out.println(accessor.getHost());
            System.out.println(accessor);
            System.out.println("사용자 연결 해제");
        }
        return message;
    }
}
