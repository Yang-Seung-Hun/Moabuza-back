package com.project.moabuja.dto.response;

import com.project.moabuja.domain.hero.HeroLevel;
import com.project.moabuja.domain.hero.HeroName;
import com.project.moabuja.domain.member.Member;
import com.project.moabuja.domain.record.Record;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class HomeResponseDto {
    private final int groupCurrentAmount;
    private final int groupNeedAmount;
    private final int groupGoalAmount;
    private final int groupGoalPercent;
    private final boolean isGroupGoal;

    private final int challengeCurrentAmount;
    private final int challengeNeedAmount;
    private final int challengeGoalAmount;
    private final int challengeGoalPercent;
    private final boolean isChallengeGoal;

    private final String nickname;
    private final HeroName heroName;
    private final HeroLevel heroLevel;

    private final int totalAmount;
    private final int wallet;

    @Builder
    public HomeResponseDto(int groupCurrentAmount, int groupNeedAmount, int groupGoalAmount, int groupGoalPercent, boolean isGroupGoal,
                           int challengeCurrentAmount, int challengeNeedAmount, int challengeGoalAmount, int challengeGoalPercent, boolean isChallengeGoal,
                           String nickname, HeroName heroName, HeroLevel heroLevel, int totalAmount, int wallet) {
        this.groupCurrentAmount = groupCurrentAmount;
        this.groupNeedAmount = groupNeedAmount;
        this.groupGoalAmount = groupGoalAmount;
        this.groupGoalPercent = groupGoalPercent;
        this.isGroupGoal = isGroupGoal;
        this.challengeCurrentAmount = challengeCurrentAmount;
        this.challengeNeedAmount = challengeNeedAmount;
        this.challengeGoalAmount = challengeGoalAmount;
        this.challengeGoalPercent = challengeGoalPercent;
        this.isChallengeGoal = isChallengeGoal;
        this.nickname = nickname;
        this.heroName = heroName;
        this.heroLevel = heroLevel;
        this.totalAmount = totalAmount;
        this.wallet = wallet;
    }

    public static HomeResponseDto of(Member member, Record record) {

        return HomeResponseDto.builder()
//                .groupCurrentAmount()
//                .groupNeedAmount()
//                .groupGoalAmount()
//                .groupGoalPercent()
//                .isGroupGoal()
//                .challengeCurrentAmount()
//                .challengeNeedAmount()
//                .challengeGoalAmount()
//                .challengeGoalPercent()
//                .isChallengeGoal()
//                .nickname()
//                .heroName()
//                .heroLevel()
//                .totalAmount()
//                .wallet()
                .build();
    }
}