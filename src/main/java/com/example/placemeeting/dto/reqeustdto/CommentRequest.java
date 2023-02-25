package com.example.placemeeting.dto.reqeustdto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

public class CommentRequest {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class CommentCreate {

        @NotBlank
        private String context;

    }
}
