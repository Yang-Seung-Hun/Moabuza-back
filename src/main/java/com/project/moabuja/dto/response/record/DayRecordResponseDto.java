package com.project.moabuja.dto.response.record;

import com.project.moabuja.domain.record.RecordType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class DayRecordResponseDto {
    /**
     * [List] dayRecords : {
     * [enum]recordType(비고 기록),
     * [LocalDateTime] recordDate(날짜),
     * [string] memos(메모),
     * [int] recordAmount(금액) }
     */
    private RecordType recordType;
    private LocalDateTime recordDate;
    private String memos;
    private int recordAmount;
}
