package com.example.placemeeting.dto.reqeustdto;

import com.example.placemeeting.domain.Member;
import com.example.placemeeting.domain.PostType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

public class PostRequest {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class PostCreate {

        @NotBlank
        private String title;

        @NotBlank
        private String context;

        private String postType;

    }
}

