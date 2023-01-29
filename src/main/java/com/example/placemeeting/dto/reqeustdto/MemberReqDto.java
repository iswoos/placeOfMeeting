package com.example.placemeeting.dto.reqeustdto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class MemberReqDto {

    @NotBlank
    private String userId;
    @NotBlank
    private String password;

    @NotBlank
    private String passwordConfirm;

    public void setEncodePwd(String encodePwd) {
        this.password = encodePwd;
    }

}
