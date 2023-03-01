package com.example.placemeeting.dto.responsedto;

import com.example.placemeeting.domain.ChatRoom;
import com.example.placemeeting.domain.PostAndChatType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ChatRoomResponse {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class ChatRoomResDto {

        private Long id;

        private String cityName; // 룸 지역

        private PostAndChatType postAndChatType; // 룸 타입

        public ChatRoomResDto(ChatRoom chatRoom) {
            this.id = chatRoom.getId();
            this.cityName = chatRoom.getCityName();
            this.postAndChatType = chatRoom.getPostAndChatType();
        }

        }
}
