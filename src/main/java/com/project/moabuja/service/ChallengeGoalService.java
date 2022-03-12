package com.project.moabuja.service;

import com.project.moabuja.domain.goal.ChallengeGoal;
import com.project.moabuja.domain.member.Member;
import com.project.moabuja.dto.request.goal.CreateChallengeRequestDto;
import com.project.moabuja.dto.response.goal.ChallengeResponseDto;
import com.project.moabuja.dto.response.goal.CreateChallengeResponseDto;

public interface ChallengeGoalService {

    public ChallengeGoal save(CreateChallengeRequestDto challengeRequestDto);

    public ChallengeResponseDto getChallengeInfo(Member currentUser);

    public CreateChallengeResponseDto getChallengeMemberCandidates(Member currentUser);

    public void exitChallenge(Long id);
}
