package com.project.moabuja.dto.response.goal;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class ChallengeResponseDto {
    private String goalStatus;
    private List<ChallengeMemberDto> challengeMembers;
    private String challengeName;
    private List<String> challengeDoneGoals;
    private List<ChallengeListDto> challengeLists = new ArrayList<>();
    private List<WaitingGoalResponseDto> waitingGoals = new ArrayList<>();

    @Builder
    public ChallengeResponseDto(String goalStatus, List<ChallengeMemberDto> challengeMembers, String challengeName, List<String> challengeDoneGoals, List<ChallengeListDto> challengeLists, List<WaitingGoalResponseDto> waitingGoals) {
        this.goalStatus = goalStatus;
        this.challengeMembers = challengeMembers;
        this.challengeName = challengeName;
        this.challengeDoneGoals = challengeDoneGoals;
        this.challengeLists = challengeLists;
        this.waitingGoals = waitingGoals;
    }
}
