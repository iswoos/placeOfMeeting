package com.example.placemeeting.domain;

import com.example.placemeeting.dto.reqeustdto.PostRequest;
import com.example.placemeeting.dto.reqeustdto.PostRequest.PostCreate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate // Dirty checking으로 인한 변경이 발생할 때, 변경 필드만 업데이트 반영되도록 해주는 어노테이션
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String context;

    @Column(nullable = false)
    private int likeNum;

    @Column(nullable = false)
    private int commentNum;

    @Column(nullable = false)
    private String cityName;

    @Column
    private String imgUrl;

    @Enumerated(EnumType.STRING)
    private PostAndChatType postType;

    public Post(Member member, PostCreate postCreate, String imgurl) {
        this.member = member;
        this.title = postCreate.getTitle();
        this.context = postCreate.getContext();
        this.imgUrl = imgurl;
        this.cityName = member.getCityName();
        this.postType = PostAndChatType.valueOf(postCreate.getPostType());
    }

    public void plusLike() {
        this.likeNum += 1;
    }

    public void minusLike() {
        this.likeNum -= 1;
    }

    public void plusComment() {
        this.commentNum += 1;
    }

    public void modifyPost(PostRequest.PostModify postModify) {
        this.title = postModify.getTitle();
        this.context = postModify.getContext();
    }
}
