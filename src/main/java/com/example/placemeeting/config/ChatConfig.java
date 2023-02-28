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

    // 한 클라이언트에서 다른 클라이언트로 메시지를 라우팅하는데 사용될 메시지 브로커를 구성
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        // /pub으로 시작하는 모든 메시지는 @MessageMapping 어노테이션이 달린 메서드로 라우팅
        registry.setApplicationDestinationPrefixes("/pub"); //메세지 보낼경로

        System.out.println("채팅이 어디로?");

        // /sub으로 시작하는 메시지가 메시지 브로커로 라우팅되도록 정의. 메시지브로커는 해당 채팅방을 구독하고 있는 클라이언트에게 메시지 전달
        registry.enableSimpleBroker("/sub");
    }

    // 클라이언트가 웹 소켓 서버에 연결하는데 사용할 웹 소켓 엔드포인트
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) { //Client에서 websocket연결할 때 사용할 API 경로를 설정해주는 메서드
        // endpint 설정 : handshake할 경로
        registry.addEndpoint("/ws/chat") //websocket연결할 때 사용할 API 경로
                .setAllowedOriginPatterns("*") //Cors 설정처리
                .withSockJS(); //SockJS
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompHandler);
    }



}
