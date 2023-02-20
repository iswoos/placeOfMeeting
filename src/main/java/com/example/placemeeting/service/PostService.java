package com.example.placemeeting.service;

import com.example.placemeeting.domain.Heart;
import com.example.placemeeting.domain.Member;
import com.example.placemeeting.domain.Post;
import com.example.placemeeting.domain.PostType;
import com.example.placemeeting.dto.reqeustdto.PostRequest.PostCreate;
import com.example.placemeeting.dto.responsedto.PostResponse.PostDetailResDto;
import com.example.placemeeting.dto.responsedto.PostResponse.PostMainResDto;
import com.example.placemeeting.exception.CustomCommonException;
import com.example.placemeeting.exception.ErrorCode;
import com.example.placemeeting.repository.HeartRepository;
import com.example.placemeeting.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    private final HeartRepository heartRepository;


    public List<PostMainResDto> getPosts(String postType, Member member) {

        List<Post> postList = postRepository.findByPostTypeAndCityName(PostType.valueOf(postType), member.getCityName());

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

    public PostDetailResDto getPost(Long postId, Member member) {
        return new PostDetailResDto(postRepository.detailPost(postId), member);
    }

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
}
