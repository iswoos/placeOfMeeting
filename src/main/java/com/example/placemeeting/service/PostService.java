package com.example.placemeeting.service;

import com.example.placemeeting.domain.*;
import com.example.placemeeting.dto.reqeustdto.PostRequest;
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

import java.util.Comparator;
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

        return postList.stream() // postList를 stream으로 변환
                .sorted(Comparator.comparing(Post::getCreatedAt).reversed()) // 생성일자 최신기준으로 정렬
                .map(PostMainResDto::new)
                /*메서드 반환타입을 맞추기 위해 map을 이용. stream의 map() 메서드는 입력 스트림의 각 요소를 다른 요소로 매핑하여,
                매핑된 요소들을 새로운 스트림으로 변환하는 용도로 편하게 사용됨*/

                //PostMainResDto::new는 클래스의 생성자를 참조하여 새로운 PostMainResDto 객체 생성함
                //map메서드에서는 각각의 객체를 받아 stream 요소에서 PostMainResDto 객체로 매핑된 요소들을 새로운 stream으로 반환한다.

                .collect(Collectors.toList());
                /*collect 메서드는 스트림의 최종 연산 중 하나로, 스트림의 요소들을 모아서 특정한 컬렉션 타입으로 반환해준다.
                매개변수는 Collector를 받으며, Collector는 스트림 요소를 수집하는 역할을 한다. Collector를 이용하여 스트림의 요소들을 모아서 List, Set, Map등의 구조로 변환할 수 있다.*/

                /*Collectors 클래스는 Collector 객체를 생성하기 위한 static 메소드를 제공한다. 활용예시는 하단에 기재.
                Collectors.toList()는 스트림 요소를 List로 수집하는 Collector를 생성
                Collectors.toSet()은 스트림 요소를 Set으로 수집하는 Collector를 생성
                Collectors.toMap()은 스트림 요소를 Map으로 수집하는 Collector를 생성*/
    }

    @Transactional
    public String createPost(PostCreate postCreate, Member member) {

        postRepository.save(new Post(member,postCreate));

        return "게시물 등록완료";
    }

    @Transactional(readOnly = true)
    public PostDetailResDto getPost(Long postId) {
        List<Comment> commentList = commentRepository.findbyPostId(postId);
        // 게시물 아이디로 가져온 댓글 리스트. stream이용하여 생성일자 기준으로 정렬 후 List반환
        List<Comment> comments = commentList.stream()
                .sorted(Comparator.comparing(Comment::getCreatedAt))
                .collect(Collectors.toList());

        return new PostDetailResDto(postRepository.detailPost(postId),comments);
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
    public String modifyPost(Long postId, PostRequest.PostModify postModify, Member member) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new CustomCommonException(ErrorCode.POST_NOT_FOUND)
        );

        if (!member.equals(post.getMember())) {
            throw new CustomCommonException(ErrorCode.UNAUTHORIZED_USER);
        }

        post.modifyPost(postModify);

        return "게시물 수정완료";
    }

    @Transactional
    public String deletePost(Long postId, Member member) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new CustomCommonException(ErrorCode.POST_NOT_FOUND)
        );

        if (!member.equals(post.getMember())) {
            throw new CustomCommonException(ErrorCode.UNAUTHORIZED_USER);
        }

        postRepository.delete(post);

        return "게시물 삭제완료";
    }
}
