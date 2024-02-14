package com.example.placemeeting.scheduler;

import com.example.placemeeting.dto.responsedto.PostResponse;
import com.example.placemeeting.service.PostService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ScheduledTasks {

    private final PostService postService;

    private final RedisTemplate redisTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Scheduled(cron ="0 0 0 * * *")
    public void mostPopularPosts() {
        List<PostResponse.mostPopularPostResDto> mostPopularPosts = postService.mostPopularPosts();

        try {
            // JSON 형태로 직렬화하여 Redis에 저장
            redisTemplate.opsForValue().set("mostPopularPosts", objectMapper.writeValueAsString(mostPopularPosts));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
