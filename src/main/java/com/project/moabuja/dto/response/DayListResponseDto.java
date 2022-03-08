package com.project.moabuja.dto.response;

import com.project.moabuja.domain.member.Member;
import com.project.moabuja.domain.record.Record;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class DayListResponseDto {
    private final List<Record> dayRecordList;
    private final int dayAmount;

    public DayListResponseDto(Record record) {
        List<Record> recordList = new ArrayList<>(); // recordList에 선택 날짜에 대한 record 값만 넣어준다.
        int incomeTotal = 0;
        int expenseTotal = 0;
//        for (int i = 0; i < recordList.size(); i++) {
//            if (recordList[i].getRecordType() == income) {
//              incomeTotal = incomeTotal + recordList[i].getRecordAmount();
//            }
//            elif (recordList[i].getRecordType() == expense) {
//              expenseTotal = expenseTotal + recordList[i].getRecordAmount();
//            }
//        }

        this.dayRecordList = recordList;
        this.dayAmount = incomeTotal - expenseTotal; // 수입 - 지출 , 추후 로직은 서비스로 이동
    }

}


//[List] dayRecords : {
//[enum]recordType(비고 기록),
//[string] recordDate(날짜),
//[string] memos(메모),
//[int] recordAmount(금액) }
//[int] dayAmount(선택한 날짜의 총 금액)