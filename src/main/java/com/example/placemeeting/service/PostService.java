package com.example.placemeeting.service;

import com.example.placemeeting.domain.*;
import com.example.placemeeting.dto.reqeustdto.PostRequest.CommentCreate;
import com.example.placemeeting.dto.reqeustdto.PostRequest.PostCreate;
import com.example.placemeeting.dto.responsedto.PostResponse.PostDetailResDto;
import com.example.placemeeting.dto.responsedto.PostResponse.PostMainResDto;
import com.example.placemeeting.exception.CustomCommonException;
import com.example.placemeeting.exception.ErrorCode;
import com.example.placemeeting.repository.CommentRepository;
import com.example.placemeeting.repository.HeartRepository;
import com.example.placemeeting.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    private final HeartRepository heartRepository;

    private final CommentRepository commentRepository;


    @Transactional(readOnly = true)
    public List<PostMainResDto> getPosts(String postType, Member member) {

        List<Post> postList = postRepository.findByPostTypeAndCityName(PostType.valueOf(postType), member.getCityName());

        // 생성일자 기준 최신순으로 정렬
        postList.sort((p1,p2) -> p2.getCreatedAt().compareTo(p1.getCreatedAt()));

        return postList.stream().map(PostMainResDto::new).collect(Collectors.toList());


//        List<PostMainResDto> responseDto = new ArrayList<>();

//        for (Post post : postList) {
//            responseDto.add(new PostMainResDto(post));
//        }
//        return responseDto;
    }

    @Transactional
    public String createPost(PostCreate postCreate, Member member) {

        postRepository.save(new Post(member,postCreate));

        return "게시물 등록완료";
    }

    @Transactional(readOnly = true)
    public PostDetailResDto getPost(Long postId, Member member) {
        return new PostDetailResDto(postRepository.detailPost(postId), member);
    }

    @Transactional
    public String likePost(Long postId, Member member) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new CustomCommonException(ErrorCode.POST_NOT_FOUND)
        );

        if (heartRepository.findByPostAndMember(post, member) == null) {
            post.plusLike();
            Heart heart = new Heart(post, member);
            heartRepository.save(heart);
            return "좋아요!";
        } else {
            Heart heart = heartRepository.findByPostAndMember(post, member);
            post.minusLike();
            heartRepository.delete(heart);
            return "좋아요 취소!";
        }
    }

    @Transactional
    public String createComment(Long postId, CommentCreate commentCreate, Member member) {

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new CustomCommonException(ErrorCode.POST_NOT_FOUND)
        );


        post.plusComment();

        commentRepository.save(new Comment(post, member, commentCreate.getContext()));

        return "댓글 등록완료";
    }
}
