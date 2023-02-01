package com.example.placemeeting.dto.reqeustdto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class MemberRequest {

    @NotBlank
    private String userId;
    @NotBlank
    private String password;
    @NotBlank
    private String phoneNumber;

    public MemberRequest(String userId, String password, String phoneNumber) {
        this.userId = userId;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    public void setEncodePwd(String encodePwd) {
        this.password = encodePwd;
    }

}
