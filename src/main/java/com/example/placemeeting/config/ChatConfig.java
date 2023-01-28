package com.example.placemeeting.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker //메시지브로커가 지원하는 WebSocket 메시지 처리
@RequiredArgsConstructor
public class ChatConfig implements WebSocketMessageBrokerConfigurer {

    private final StompHandler stompHandler;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        registry.enableSimpleBroker("/sub");
        System.out.println("채팅이 어디로?");
        registry.setApplicationDestinationPrefixes("/pub"); //메세지 보낼경로
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) { //Client에서 websocket연결할 때 사용할 API 경로를 설정해주는 메서드
        registry.addEndpoint("/ws/chat") //websocket연결할 때 사용할 API 경로
                .setAllowedOriginPatterns("*") //Cors 처리
                .withSockJS(); //SockJS
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompHandler);
    }



}
