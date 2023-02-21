package com.example.placemeeting.domain;

import com.example.placemeeting.dto.reqeustdto.PostRequest;
import com.example.placemeeting.dto.reqeustdto.PostRequest.PostCreate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Enumerated(EnumType.STRING)
    private PostType postType;

    public Post(Member member, PostCreate postCreate) {
        this.member = member;
        this.title = postCreate.getTitle();
        this.context = postCreate.getContext();
        this.cityName = member.getCityName();
        this.postType = PostType.valueOf(postCreate.getPostType());
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
}
