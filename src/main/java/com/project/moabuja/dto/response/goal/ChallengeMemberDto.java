package com.project.moabuja.dto.response.goal;

import com.project.moabuja.domain.member.Hero;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChallengeMemberDto {
    private String challengeMemberNickname;
    private Hero challengeMemberHero;
    private int challengeMemberLeftAmount;
    private int challengeMemberNowPercent;

    @Builder
    public ChallengeMemberDto(String challengeMemberNickname, Hero challengeMemberHero, int challengeMemberLeftAmount, int challengeMemberNowPercent) {
        this.challengeMemberNickname = challengeMemberNickname;
        this.challengeMemberHero = challengeMemberHero;
        this.challengeMemberLeftAmount = challengeMemberLeftAmount;
        this.challengeMemberNowPercent = challengeMemberNowPercent;
    }
}
