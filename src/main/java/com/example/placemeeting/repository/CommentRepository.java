package com.example.placemeeting.repository;

import com.example.placemeeting.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findbyPostId(Long postId);
}
