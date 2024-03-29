package com.example.placemeeting.dto.responsedto;

import com.example.placemeeting.domain.ChatRoom;
import com.example.placemeeting.domain.PostAndChatType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.LinkedList;

public class ChatRoomResponse {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class ChatRoomResDto {

        private Long id;

        private String cityName; // 룸 지역

        private PostAndChatType postAndChatType; // 룸 타입

        private String ChatType;

        public ChatRoomResDto(ChatRoom chatRoom) {
            this.id = chatRoom.getId();
            this.cityName = chatRoom.getCityName();
            this.postAndChatType = chatRoom.getPostAndChatType();
            this.ChatType = chatRoom.getPostAndChatType().getValue();
        }
    }
}
