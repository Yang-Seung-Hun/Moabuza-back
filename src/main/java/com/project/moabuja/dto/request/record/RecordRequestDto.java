package com.project.moabuja.dto.request.record;

import com.project.moabuja.domain.record.Record;
import com.project.moabuja.domain.record.RecordType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class RecordRequestDto {
    private RecordType recordType;
    private String recordDate;
    private String memos;
    @Positive
    private int recordAmount;

    @Builder
    public RecordRequestDto(RecordType recordType, String recordDate, String memos, int recordAmount) {
        this.recordType = recordType;
        this.recordDate = recordDate;
        this.memos = memos;
        this.recordAmount = recordAmount;
    }
}
