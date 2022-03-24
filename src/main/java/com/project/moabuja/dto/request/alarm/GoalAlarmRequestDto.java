package com.project.moabuja.dto.request.alarm;

import com.project.moabuja.domain.alarm.AlarmDetailType;
import com.project.moabuja.domain.alarm.AlarmType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class GoalAlarmRequestDto {

    private String goalName;
    private int goalAmount;
    private List<String> friendNickname;

    @Builder
    public GoalAlarmRequestDto(String goalName, int goalAmount, List<String> friendNickname) {
        this.goalName = goalName;
        this.goalAmount = goalAmount;
        this.friendNickname = friendNickname;
    }
}
