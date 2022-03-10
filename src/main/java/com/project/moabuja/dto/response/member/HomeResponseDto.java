package com.project.moabuja.dto.response.member;

import com.project.moabuja.domain.hero.HeroLevel;
import com.project.moabuja.domain.hero.HeroName;
import com.project.moabuja.domain.member.Member;
import com.project.moabuja.domain.record.Record;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class HomeResponseDto {
    private int groupCurrentAmount;
    private int groupNeedAmount;
    private int groupAmount;
    private int groupPercent;
    private String groupName;

    private int challengeCurrentAmount;
    private int challengeNeedAmount;
    private int challengeAmount;
    private int challengePercent;
    private String challengeName;

    private HeroName heroName;
    private HeroLevel heroLevel;

    private int totalAmount;
    private int wallet;

}