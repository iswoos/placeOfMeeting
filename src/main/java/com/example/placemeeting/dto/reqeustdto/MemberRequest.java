package com.example.placemeeting.dto.reqeustdto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor
public class MemberRequest {

    @NotBlank
    private String userId;
    @NotBlank
    private String password;

    @NotBlank
    private String userName;

    @NotBlank
    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "핸드폰 번호의 약식과 맞지 않습니다. xxx-xxxx-xxxx")
    private String phoneNumber;

    public MemberRequest(String userId, String password, String userName, String phoneNumber) {
        this.userId = userId;
        this.password = password;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
    }

    public void setEncodePwd(String encodePwd) {
        this.password = encodePwd;
    }

}
