package com.project.moabuja.dto.response.record;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class DayListResponseDto {

    private List<DayRecordResponseDto> dayRecordList;
    private int dayIncomeAmount;
    private int dayExpenseAmount;
    private int dayChallengeAmount;
    private int dayGroupAmount;

    @Builder
    public DayListResponseDto(List<DayRecordResponseDto> dayRecordList, int dayIncomeAmount, int dayExpenseAmount, int dayChallengeAmount, int dayGroupAmount) {
        this.dayRecordList = dayRecordList;
        this.dayIncomeAmount = dayIncomeAmount;
        this.dayExpenseAmount = dayExpenseAmount;
        this.dayChallengeAmount = dayChallengeAmount;
        this.dayGroupAmount = dayGroupAmount;
    }
}