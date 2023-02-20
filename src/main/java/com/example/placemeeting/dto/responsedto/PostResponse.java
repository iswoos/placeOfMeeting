package com.example.placemeeting.dto.responsedto;

import com.example.placemeeting.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class PostResponse {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class PostMainResDto {

        private String userName;

        private String title;

        private int likeNum;

        private int commentNum;

        private String cityName;

        private LocalDateTime createdAt;

        public PostMainResDto(Post post) {
            this.userName = post.getMember().getUserName();
            this.title = post.getTitle();
            this.likeNum = post.getLikeNum();
            this.commentNum = post.getCommentNum();
            this.createdAt = post.getCreatedAt();
            this.cityName = post.getCityName();
        }

    }

}
