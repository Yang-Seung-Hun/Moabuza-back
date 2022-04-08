package com.project.moabuja.dto.response.alarm;

import com.project.moabuja.domain.alarm.Alarm;
import com.project.moabuja.domain.alarm.AlarmDetailType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GoalAlarmResponseDto {
    private Long alarmId;
    private AlarmDetailType alarmDetailType;
    private String friendNickname;
    private String goalName;
    private int goalAmount;

    @Builder
    public GoalAlarmResponseDto(Long alarmId, AlarmDetailType alarmDetailType, String friendNickname, String goalName, int goalAmount) {
        this.alarmId = alarmId;
        this.alarmDetailType = alarmDetailType;
        this.friendNickname = friendNickname;
        this.goalName = goalName;
        this.goalAmount = goalAmount;
    }

    public static GoalAlarmResponseDto of(Alarm alarm) {
        return GoalAlarmResponseDto.builder()
                .alarmId(alarm.getId())
                .alarmDetailType(alarm.getAlarmDetailType())
                .friendNickname(alarm.getFriendNickname())
                .goalName(alarm.getGoalName())
                .goalAmount(alarm.getGoalAmount())
                .build();
    }
}
