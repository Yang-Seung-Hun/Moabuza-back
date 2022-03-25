package com.project.moabuja.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CalenderRequestDto {
    private LocalDateTime recordMonth;
}
