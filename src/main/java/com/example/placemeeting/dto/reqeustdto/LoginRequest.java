package com.example.placemeeting.dto.reqeustdto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class LoginRequest {

    @NotBlank
    private String userId;
    @NotBlank
    private String password;

    private Double latitude;

    private Double longitude;

    public LoginRequest(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    public LoginRequest(String userId, String password, double latitude, double longitude) {
        this.userId = userId;
        this.password = password;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}

