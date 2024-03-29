package com.example.placemeeting.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import com.example.placemeeting.config.CommonUtils;
import com.example.placemeeting.domain.*;
import com.example.placemeeting.dto.reqeustdto.CommentRequest.getComment;
import com.example.placemeeting.dto.reqeustdto.PostRequest;
import com.example.placemeeting.dto.responsedto.PostResponse;
import com.example.placemeeting.dto.responsedto.PostResponse.PostDetailResDto;
import com.example.placemeeting.dto.responsedto.PostResponse.PostMainResDto;
import com.example.placemeeting.exception.CustomCommonException;
import com.example.placemeeting.exception.ErrorCode;
import com.example.placemeeting.repository.CommentRepository;
import com.example.placemeeting.repository.HeartRepository;
import com.example.placemeeting.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    private final HeartRepository heartRepository;

    private final CommentRepository commentRepository;

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Transactional(readOnly = true)
    public List<PostMainResDto> getPosts(String postType, Member member) {

        List<Post> postList = postRepository.findByPostTypeAndCityName(PostAndChatType.valueOf(postType), member.getCityName());

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
    public String createPost(MultipartFile file ,PostRequest.PostCreate postCreate, Member member) throws IOException {

        String imgurl = "";

        if (file != null && !file.isEmpty()) {
            String fileName = CommonUtils.buildFileName(file.getOriginalFilename());
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(file.getContentType());

            byte[] bytes = IOUtils.toByteArray(file.getInputStream());
            objectMetadata.setContentLength(bytes.length);
            ByteArrayInputStream byteArrayIs = new ByteArrayInputStream(bytes);

            amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, byteArrayIs, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));

            imgurl = amazonS3Client.getUrl(bucketName, fileName).toString();
        }

        postRepository.save(new Post(member, postCreate, imgurl));

        return "게시물 등록완료";
    }

    @Transactional(readOnly = true)
    public PostDetailResDto getPost(Long postId) {
        List<Comment> commentList = commentRepository.findByPostId(postId);
        // 게시물 아이디로 가져온 댓글 리스트. stream이용하여 생성일자 최신기준으로 정렬 후 List반환
        List<getComment> comments = commentList.stream()
                .sorted(Comparator.comparing(Comment::getCreatedAt).reversed())
                .map(getComment::new)
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

    @Transactional(readOnly = true)
    public List<PostResponse.mostPopularPostResDto> mostPopularPosts() {
        // 각 타입별로 최대 10개의 게시물을 가져오는 Map을 초기화합니다.
        Map<PostAndChatType, List<Post>> popularPostsMap = new HashMap<>();
        for (PostAndChatType type : PostAndChatType.values()) {
            popularPostsMap.put(type, new ArrayList<>());
        }

        // 7일 이내의 모든 게시물을 가져옵니다.
        List<Post> mostPopularPostList = postRepository.findByCreatedAtAfterOrderByLikeNumDescCreatedAtDesc(LocalDateTime.now().minusDays(7));

        // 타입별로 최대 10개까지만 가져와서 Map에 추가합니다.
        for (Post post : mostPopularPostList) {
            List<Post> postsOfType = popularPostsMap.get(post.getPostType());
            if (postsOfType.size() < 10) {
                postsOfType.add(post);
            }
        }

        // Map에 있는 모든 게시물을 모아서 리스트로 반환합니다.
        List<PostResponse.mostPopularPostResDto> result = new ArrayList<>();
        for (List<Post> postsOfType : popularPostsMap.values()) {
            result.addAll(postsOfType.stream()
                    .map(post -> new PostResponse.mostPopularPostResDto(post.getId(), post.getTitle(), post.getPostType()))
                    .collect(Collectors.toList()));
        }

        return result;
    }









//        List<Post> mostPopularPostList = postRepository.findByCreatedAtAfterOrderByLikeNumDescCreatedAtDesc(LocalDateTime.now().minusDays(7));
//
//        return mostPopularPostList.stream()
//                .map(post -> new PostResponse.mostPopularPostResDto(post.getId(), post.getTitle(), post.getPostType()))
//                .collect(Collectors.toList());

}
