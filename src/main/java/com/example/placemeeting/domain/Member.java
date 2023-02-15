package com.example.placemeeting.domain;

import com.example.placemeeting.dto.reqeustdto.MemberRequest;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;
    @NotBlank
    private String userId;
    @NotBlank
    private String password;

    @NotBlank
    private String userName;

    @NotBlank
    private String phoneNumber;

    private Double latitude;

    private Double longitude;

    public void GeolocationSet(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Member(MemberRequest memberRequest) {
        this.userId = memberRequest.getUserId();
        this.password = memberRequest.getPassword();
        this.userName = memberRequest.getUserName();
        this.phoneNumber = memberRequest.getPhoneNumber();
    }

}
