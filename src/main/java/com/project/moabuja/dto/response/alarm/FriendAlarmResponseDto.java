package com.project.moabuja.dto.response.alarm;

import com.project.moabuja.domain.alarm.Alarm;
import com.project.moabuja.domain.alarm.AlarmDetailType;
import com.project.moabuja.domain.alarm.AlarmType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class FriendAlarmResponseDto {
    private Long alarmId;
    private AlarmDetailType alarmDetailType;
    private String friendNickname;

    public FriendAlarmResponseDto(Long alarmId, AlarmDetailType alarmDetailType, String friendNickname) {
        this.alarmId = alarmId;
        this.alarmDetailType = alarmDetailType;
        this.friendNickname = friendNickname;
    }
}
