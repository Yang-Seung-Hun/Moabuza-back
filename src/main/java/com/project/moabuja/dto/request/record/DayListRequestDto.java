package com.project.moabuja.dto.request.record;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class DayListRequestDto {
    private String recordDate;

    public DayListRequestDto(String recordDate) {
        this.recordDate = recordDate;
    }
}
