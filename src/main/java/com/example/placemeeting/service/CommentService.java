package com.example.placemeeting.service;


import com.example.placemeeting.domain.Comment;
import com.example.placemeeting.domain.Member;
import com.example.placemeeting.domain.Post;
import com.example.placemeeting.dto.reqeustdto.CommentRequest;
import com.example.placemeeting.dto.reqeustdto.PostRequest;
import com.example.placemeeting.exception.CustomCommonException;
import com.example.placemeeting.exception.ErrorCode;
import com.example.placemeeting.repository.CommentRepository;
import com.example.placemeeting.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public String createComment(Long postId, CommentRequest.CommentCreate commentCreate, Member member) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new CustomCommonException(ErrorCode.POST_NOT_FOUND)
        );

        post.plusComment();

        commentRepository.save(new Comment(post, member, commentCreate.getContext()));

        return "댓글 등록완료";
    }
}
