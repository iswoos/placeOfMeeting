package com.example.placemeeting.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage extends BaseEntity { //뷰로 보내는 메세지

    @Id
    @Column(name = "chatmessage_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public enum MessageType {
        ENTER, TALK, QUIT
    }

    private MessageType type;
    //채팅방 ID
    private String roomId;
    //보내는 사람
    private String sender;
    //내용
    private String message;

    private String sendTime;
}
