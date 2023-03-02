package com.example.placemeeting.repository;

import com.example.placemeeting.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    List<ChatRoom> findByCityName(String cityName);

}
