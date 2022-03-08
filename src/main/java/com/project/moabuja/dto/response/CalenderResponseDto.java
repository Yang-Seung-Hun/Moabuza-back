package com.project.moabuja.dto.response;

import com.project.moabuja.domain.record.Record;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CalenderResponseDto {
    private final List<Record> monthRecordList;
//    List 형식을 Record 말고, monthRecordList 라는 객체를 만들어서 사용하는 게 좋을 것 같다
//    날짜별로 하루 수입 합계, 하루 지출 합계, 하루 목표 합계(challenge + group) 가 들어가야 한다

    public CalenderResponseDto(Record record) {
        List<Record> recordList = new ArrayList<>(); // 날짜가 month에 해당하는 list 만들어서 보내준다
        this.monthRecordList = recordList;
    }
}
