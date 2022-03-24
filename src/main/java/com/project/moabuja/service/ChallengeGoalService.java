package com.project.moabuja.service;

import com.project.moabuja.domain.goal.ChallengeGoal;
import com.project.moabuja.domain.member.Member;
import com.project.moabuja.dto.request.goal.CreateChallengeRequestDto;
import com.project.moabuja.dto.response.goal.ChallengeResponseDto;
import com.project.moabuja.dto.response.goal.CreateChallengeResponseDto;
import org.springframework.http.ResponseEntity;

public interface ChallengeGoalService {

    ResponseEntity<String> save(CreateChallengeRequestDto challengeRequestDto, Member currentUser);

    ResponseEntity<ChallengeResponseDto> getChallengeInfo(Member currentUser);

    ResponseEntity<CreateChallengeResponseDto> getChallengeMemberCandidates(Member currentUser);

    ResponseEntity<String> exitChallenge(Long id);
}
