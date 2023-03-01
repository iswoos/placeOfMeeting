package com.example.placemeeting.dto.reqeustdto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ChatRoomRequest {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class ChatRoomCreate {

        private String chatType;

    }

}
