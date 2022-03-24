package com.project.moabuja.dto.response.goal;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WaitingGoalDto {

    private Long id;
    private String waitingGoalName;

    public WaitingGoalDto(Long id, String waitingGoalName) {
        this.id = id;
        this.waitingGoalName = waitingGoalName;
    }
}
