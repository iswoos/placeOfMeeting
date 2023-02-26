package com.example.placemeeting.dto.reqeustdto;

import com.example.placemeeting.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class CommentRequest {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class CommentCreate {

        @NotBlank
        private String context;

    }


    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class getComment {
        private String context;

        private String userName;

        private LocalDateTime createdAt;

        public getComment(Comment comment) {
            this.context = comment.getContext();
            this.userName = comment.getUserName();
            this.createdAt = comment.getCreatedAt();
        }


    }
}

