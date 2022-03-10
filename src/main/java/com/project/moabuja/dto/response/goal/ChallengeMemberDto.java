package com.project.moabuja.dto.response.goal;

import com.project.moabuja.domain.hero.HeroName;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChallengeMemberDto {
    private String challengeMemberNickname;
    private HeroName challengeMemberHero;
    private int challengeMemberLeftAmount;
    private int challengeMemberNowPercent;
}
