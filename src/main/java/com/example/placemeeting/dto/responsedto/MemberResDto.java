package com.example.placemeeting.dto.responsedto;

import com.example.placemeeting.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MemberResDto {
    private Long id;
    private String userId;

    public MemberResDto(Member member) {
        this.id = member.getId();
        this.userId = member.getUserId();
    }
}
