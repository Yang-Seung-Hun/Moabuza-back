package com.project.moabuja.dto.request.record;

import com.project.moabuja.domain.record.RecordType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class RecordRequestDto {
    private RecordType recordType;
    private LocalDateTime recordDate;
    private String memos;
    private int recordAmount;

    @Builder
    public RecordRequestDto(RecordType recordType, LocalDateTime recordDate, String memos, int recordAmount) {
        this.recordType = recordType;
        this.recordDate = recordDate;
        this.memos = memos;
        this.recordAmount = recordAmount;
    }
}
