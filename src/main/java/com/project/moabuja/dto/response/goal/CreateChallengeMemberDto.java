package com.project.moabuja.dto.response.goal;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateChallengeMemberDto {

    private String challengeMemberNickname;
    private boolean challengeMemberCanInvite;

    public CreateChallengeMemberDto(String challengeMemberNickname, boolean challengeMemberCanInvite) {
        this.challengeMemberNickname = challengeMemberNickname;
        this.challengeMemberCanInvite = challengeMemberCanInvite;
    }
}
