package com.example.placemeeting.service;

import com.example.placemeeting.domain.Member;
import com.example.placemeeting.domain.Post;
import com.example.placemeeting.domain.PostType;
import com.example.placemeeting.dto.reqeustdto.PostRequest;
import com.example.placemeeting.dto.reqeustdto.PostRequest.PostCreate;
import com.example.placemeeting.dto.responsedto.PostResponse;
import com.example.placemeeting.dto.responsedto.PostResponse.PostDetailResDto;
import com.example.placemeeting.dto.responsedto.PostResponse.PostMainResDto;
import com.example.placemeeting.global.dto.ResponseDto;
import com.example.placemeeting.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;


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
}
