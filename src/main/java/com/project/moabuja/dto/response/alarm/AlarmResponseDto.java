package com.project.moabuja.dto.response.alarm;

import com.project.moabuja.domain.alarm.AlarmType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class AlarmResponseDto {
    private AlarmType alarmType;
    private LocalDateTime alarmTime;
    private String alarmContents;
    private String fromNickname;

    @Builder
    public AlarmResponseDto(AlarmType alarmType, LocalDateTime alarmTime, String alarmContents, String fromNickname) {
        this.alarmType = alarmType;
        this.alarmTime = alarmTime;
        this.alarmContents =alarmContents;
        this.fromNickname = fromNickname;
    }
}
