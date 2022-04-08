package com.project.moabuja.dto.response.record;

import com.project.moabuja.domain.record.RecordType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class DayRecordResponseDto {

    private Long id;
    private RecordType recordType;
    private LocalDateTime recordDate;
    private String memos;
    private int recordAmount;

    @Builder
    public DayRecordResponseDto(Long id, RecordType recordType, LocalDateTime recordDate, String memos, int recordAmount) {
        this.id = id;
        this.recordType = recordType;
        this.recordDate = recordDate;
        this.memos = memos;
        this.recordAmount = recordAmount;
    }
}
