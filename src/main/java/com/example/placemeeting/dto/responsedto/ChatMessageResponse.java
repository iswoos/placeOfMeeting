package com.example.placemeeting.dto.responsedto;

import com.example.placemeeting.domain.ChatMessage;
import com.example.placemeeting.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

public class ChatMessageResponse {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class ChatMessageResDto {

        public enum MessageType {
            ENTER, TALK, QUIT
        }

        private ChatMessageResDto.MessageType type;

        private String roomId;

        private String sender;
        //내용
        private String message;

        private String sendTime;

    }
}
