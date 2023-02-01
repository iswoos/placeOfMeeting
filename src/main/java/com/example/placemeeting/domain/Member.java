package com.example.placemeeting.domain;

import com.example.placemeeting.dto.reqeustdto.MemberRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Getter
@Entity
@NoArgsConstructor
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String userId;
    @NotBlank
    private String password;
    @NotBlank
    private String phoneNumber;

    public Member(MemberRequest memberRequest) {
        this.userId = memberRequest.getUserId();
        this.password = memberRequest.getPassword();
        this.phoneNumber = memberRequest.getPhoneNumber();
    }

}
