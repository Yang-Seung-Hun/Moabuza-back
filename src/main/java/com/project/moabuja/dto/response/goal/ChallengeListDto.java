package com.project.moabuja.dto.response.goal;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ChallengeListDto {

    private LocalDateTime challengeRecordDate;
    private String challengeMemo;
    private int challengeAmount;

    public ChallengeListDto(LocalDateTime challengeRecordDate, String challengeMemo, int challengeAmount) {
        this.challengeRecordDate = challengeRecordDate;
        this.challengeMemo = challengeMemo;
        this.challengeAmount = challengeAmount;
    }
}
