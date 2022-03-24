package com.project.moabuja.dto.response.goal;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WaitingGoalResponseDto {

    private Long id;
    private String waitingGoalName;

    public WaitingGoalResponseDto(Long id, String waitingGoalName) {
        this.id = id;
        this.waitingGoalName = waitingGoalName;
    }
}
