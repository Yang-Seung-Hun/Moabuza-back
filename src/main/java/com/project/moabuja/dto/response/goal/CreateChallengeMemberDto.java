package com.project.moabuja.dto.response.goal;

import com.project.moabuja.domain.member.Hero;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateChallengeMemberDto {

    private String challengeMemberNickname;
    private boolean challengeMemberCanInvite;
    private Hero hero;

    public CreateChallengeMemberDto(String challengeMemberNickname, boolean challengeMemberCanInvite, Hero hero) {
        this.challengeMemberNickname = challengeMemberNickname;
        this.challengeMemberCanInvite = challengeMemberCanInvite;
        this.hero = hero;
    }
}
