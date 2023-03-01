package com.example.placemeeting.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class ChatRoom extends BaseEntity {

    @Id
    @Column(name = "chatroom_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cityName; // 룸 지역

    @Enumerated(EnumType.STRING)
    private PostAndChatType postAndChatType; // 룸 타입

    public ChatRoom(String chatType, Member member) {
        this.cityName = member.getCityName();
        this.postAndChatType = PostAndChatType.valueOf(chatType);
    }
}
