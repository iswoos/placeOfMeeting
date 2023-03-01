package com.example.placemeeting.repository;

import com.example.placemeeting.domain.Post;
import com.example.placemeeting.domain.PostAndChatType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByPostTypeAndCityName(PostAndChatType postType, String cityName);

    @Query("select p from Post p where p.id = :postId")
    Post detailPost(@Param("postId") Long postId);
}
