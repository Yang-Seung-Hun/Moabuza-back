package com.project.moabuja.dto.request.goal;

import com.project.moabuja.domain.goal.GoalType;
import com.project.moabuja.domain.goal.WaitingGoal;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WaitingGoalSaveDto {

    private String waitingGoalName;
    private int waitingGoalAmount;
    private boolean isAcceptedGoal;
    private GoalType goalType;

    @Builder
    public WaitingGoalSaveDto(String waitingGoalName, int waitingGoalAmount, boolean isAcceptedGoal, GoalType goalType) {
        this.waitingGoalName = waitingGoalName;
        this.waitingGoalAmount = waitingGoalAmount;
        this.isAcceptedGoal = isAcceptedGoal;
        this.goalType = goalType;
    }

    public static WaitingGoal toEntity(String waitingGoalName, int waitingGoalAmount, boolean isAcceptedGoal, GoalType goalType) {
        return WaitingGoal.builder()
                .waitingGoalName(waitingGoalName)
                .waitingGoalAmount(waitingGoalAmount)
                .isAcceptedGoal(isAcceptedGoal)
                .goalType(goalType)
                .build();
    }
}
