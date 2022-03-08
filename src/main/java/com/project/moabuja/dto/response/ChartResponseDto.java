package com.project.moabuja.dto.response;

import com.project.moabuja.domain.record.Record;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ChartResponseDto {
    private final List<Record> monthRecordList;
//    List 형식을 Record 말고, monthRecordList 라는 객체를 만들어서 사용하는 게 좋을 것 같다
//    날짜별로 하루 수입 합계, 하루 지출 합계가 들어가야 한다 (달력에서 쓰는 하루 목표 합계 데이터만 0으로 채워서 넘겨도 됨)
    private final int monthIncomeAmount;
    private final int monthExpenseAmount;
    private final int monthWalletAmount;
    private final int monthGoalAmount;

    @Builder
    public ChartResponseDto(List<Record> monthRecordList, int monthIncomeAmount, int monthExpenseAmount,
                            int monthWalletAmount, int monthGoalAmount) {
        this.monthRecordList = monthRecordList;
        this.monthIncomeAmount = monthIncomeAmount;
        this.monthExpenseAmount = monthExpenseAmount;
        this.monthWalletAmount = monthWalletAmount;
        this.monthGoalAmount = monthGoalAmount;
    }

    public static ChartResponseDto of(Record record) {

        return ChartResponseDto.builder()
//                .monthRecordList()
//                .monthIncomeAmount()
//                .monthExpenseAmount()
//                .monthWalletAmount()
//                .monthGoalAmount()
                .build();
    }

}
