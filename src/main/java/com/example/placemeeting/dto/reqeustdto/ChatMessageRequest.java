package com.example.placemeeting.dto.reqeustdto;


import com.example.placemeeting.domain.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ChatMessageRequest {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class ChatMessageCreate {

        public enum MessageType {
            ENTER, TALK, QUIT
        }

        private ChatMessage.MessageType type;
        //채팅방 ID
        private String roomId;
        //보내는 사람
        private String sender;
        //내용
        private String message;

        private String sendTime;

        public ChatMessageCreate(ChatMessage chatMessage) {
            this.type = chatMessage.getType();
            this.roomId = chatMessage.getRoomId();
            this.sender = chatMessage.getSender();
            this.message = chatMessage.getMessage();
            this.sendTime = chatMessage.getSendTime();
        }

    }
}
