package com.example.placemeeting.service;

import com.example.placemeeting.domain.ChatRoom;
import com.example.placemeeting.domain.Member;
import com.example.placemeeting.dto.reqeustdto.ChatRoomRequest.ChatRoomCreate;
import com.example.placemeeting.dto.responsedto.ChatRoomResponse.ChatRoomResDto;
import com.example.placemeeting.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {

    private Map<String, ChatRoom> chatRooms;

    private final ChatRoomRepository chatRoomRepository;

    @PostConstruct
    //의존관게 주입완료되면 실행되는 코드
    private void init() {
        chatRooms = new LinkedHashMap<>();
    }

    //채팅방 불러오기
    public List<ChatRoomResDto> findAllRoom(Member member) {
        //채팅방 최근 생성 순으로 반환
        List<ChatRoom> chatRoomList = chatRoomRepository.findByCityName(member.getCityName());

        return chatRoomList.stream()
                .map(ChatRoomResDto::new)
                .collect(Collectors.toList());
    }

    //채팅방 하나 불러오기
    public ChatRoom findById(Long roomId) {
        return chatRooms.get(roomId);
    }

    //채팅방 생성
    public ChatRoom createRoom(ChatRoomCreate chatRoomCreate, Member member) {
        ChatRoom chatRoom = new ChatRoom(chatRoomCreate.getChatType(), member);
        chatRoomRepository.save(chatRoom);
        return chatRoom;
    }
}
