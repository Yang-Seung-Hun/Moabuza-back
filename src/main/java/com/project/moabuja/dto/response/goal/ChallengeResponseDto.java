package com.project.moabuja.dto.response.goal;

import java.util.List;

public class ChallengeResponseDto {
    private String goalStatus;
    private List<ChallengeMemberDto> challengeMembers;
    private String challengeName;
    private List<String> challengeDoneGoals;

    public ChallengeResponseDto(String goalStatus, List<ChallengeMemberDto> challengeMembers, String challengeName, List<String> challengeDoneGoals) {
        this.goalStatus = goalStatus;
        this.challengeMembers = challengeMembers;
        this.challengeName = challengeName;
        this.challengeDoneGoals = challengeDoneGoals;
    }
}
