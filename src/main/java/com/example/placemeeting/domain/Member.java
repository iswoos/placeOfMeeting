package com.example.placemeeting.domain;

import com.example.placemeeting.dto.reqeustdto.MemberRequest;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer"})
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

    private String cityName;

    public void GeolocationSet(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void localSet(String cityName) {
        this.cityName = cityName;
    }

    public Member(MemberRequest memberRequest) {
        this.userId = memberRequest.getUserId();
        this.password = memberRequest.getPassword();
        this.userName = memberRequest.getUserName();
        this.phoneNumber = memberRequest.getPhoneNumber();
    }
}
