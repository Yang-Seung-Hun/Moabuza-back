package com.project.moabuja.dto.response.member;

import com.project.moabuja.domain.member.Hero;
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

    private Hero hero;
    private int heroLevel;

    private int totalAmount;
    private int wallet;

    private int alarmCount;


    public HomeResponseDto(int groupCurrentAmount, int groupNeedAmount, int groupAmount, int groupPercent,
                           String groupName, int challengeCurrentAmount, int challengeNeedAmount,
                           int challengeAmount, int challengePercent, String challengeName, Hero hero,
                           int heroLevel, int totalAmount, int wallet, int alarmCount) {
        this.groupCurrentAmount = groupCurrentAmount;
        this.groupNeedAmount = groupNeedAmount;
        this.groupAmount = groupAmount;
        this.groupPercent = groupPercent;
        this.groupName = groupName;
        this.challengeCurrentAmount = challengeCurrentAmount;
        this.challengeNeedAmount = challengeNeedAmount;
        this.challengeAmount = challengeAmount;
        this.challengePercent = challengePercent;
        this.challengeName = challengeName;
        this.hero = hero;
        this.heroLevel = heroLevel;
        this.totalAmount = totalAmount;
        this.wallet = wallet;
        this.alarmCount = alarmCount;
    }
}