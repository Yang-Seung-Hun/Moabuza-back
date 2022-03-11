package com.project.moabuja.service;

import com.project.moabuja.domain.goal.ChallengeGoal;
import com.project.moabuja.dto.request.goal.CreateChallengeRequestDto;

public interface ChallengeGoalService {

    public ChallengeGoal save(CreateChallengeRequestDto challengeRequestDto);

}
