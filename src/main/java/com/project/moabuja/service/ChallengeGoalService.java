package com.project.moabuja.service;

import com.project.moabuja.domain.goal.ChallengeGoal;
import com.project.moabuja.domain.member.Member;
import com.project.moabuja.dto.request.goal.CreateChallengeRequestDto;
import com.project.moabuja.dto.response.goal.ChallengeResponseDto;
import com.project.moabuja.dto.response.goal.CreateChallengeResponseDto;
import org.springframework.http.ResponseEntity;

public interface ChallengeGoalService {

    public ResponseEntity<String> save(CreateChallengeRequestDto challengeRequestDto, Member currentUser);

    public ResponseEntity<ChallengeResponseDto> getChallengeInfo(Member currentUser);

    public ResponseEntity<CreateChallengeResponseDto> getChallengeMemberCandidates(Member currentUser);

    public ResponseEntity<String> exitChallenge(Long id);
}
