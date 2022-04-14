package com.project.moabuja.dto.response.member;

import com.project.moabuja.domain.member.Hero;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class HomeResponseDto {
    private int groupCurrentAmount;
    private int groupNeedAmount;
    private int groupAmount;
    private int groupPercent;
    private String groupName;
    private int groupGoalAmount;

    private int challengeCurrentAmount;
    private int challengeNeedAmount;
    private int challengeAmount;
    private int challengePercent;
    private String challengeName;
    private int challengeGoalAmount;

    private Hero hero;
    private String nickname;

    private int totalAmount;
    private int wallet;

    private int alarmCount;

    private boolean isFirstLogin;

    @Builder
    public HomeResponseDto(int groupCurrentAmount, int groupNeedAmount, int groupPercent,
                           String groupName, int groupGoalAmount, int challengeCurrentAmount, int challengeNeedAmount,
                           int challengePercent, String challengeName, int challengeGoalAmount,
                           Hero hero, String nickname, int totalAmount, int wallet, int alarmCount, boolean isFirstLogin) {
        this.groupCurrentAmount = groupCurrentAmount;
        this.groupNeedAmount = groupNeedAmount;
        this.groupPercent = groupPercent;
        this.groupName = groupName;
        this.groupGoalAmount = groupGoalAmount;
        this.challengeCurrentAmount = challengeCurrentAmount;
        this.challengeNeedAmount = challengeNeedAmount;
        this.challengePercent = challengePercent;
        this.challengeName = challengeName;
        this.challengeGoalAmount = challengeGoalAmount;
        this.hero = hero;
        this.nickname = nickname;
        this.totalAmount = totalAmount;
        this.wallet = wallet;
        this.alarmCount = alarmCount;
        this.isFirstLogin = isFirstLogin;
    }
}