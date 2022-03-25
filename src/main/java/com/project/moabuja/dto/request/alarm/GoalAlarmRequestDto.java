package com.project.moabuja.dto.request.alarm;

import com.project.moabuja.domain.goal.GoalType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;
import java.util.List;

@Getter
@NoArgsConstructor
public class GoalAlarmRequestDto {

    private GoalType goalType;
    private String goalName;
    @Positive
    private int goalAmount;
    private List<String> friendNickname;

    @Builder
    public GoalAlarmRequestDto(GoalType goalType, String goalName, int goalAmount, List<String> friendNickname) {
        this.goalType = goalType;
        this.goalName = goalName;
        this.goalAmount = goalAmount;
        this.friendNickname = friendNickname;
    }
}
