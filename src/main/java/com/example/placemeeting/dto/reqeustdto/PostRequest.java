package com.example.placemeeting.dto.reqeustdto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

public class PostRequest {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class PostCreate {

        @NotBlank(message = "제목을 입력해주세요")
        private String title;

        @NotBlank(message = "글을 입력해주세요")
        private String context;

        private String postType;

    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class PostModify {

        @NotBlank(message = "제목을 입력해주세요")
        private String title;

        @NotBlank(message = "글을 입력해주세요")
        private String context;
    }
}

