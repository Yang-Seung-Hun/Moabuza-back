package com.project.moabuja.service;

import com.project.moabuja.domain.member.Member;
import com.project.moabuja.dto.request.alarm.GoalAlarmRequestDto;
import com.project.moabuja.dto.request.goal.CreateChallengeRequestDto;
import com.project.moabuja.dto.response.goal.ChallengeResponseDto;
import com.project.moabuja.dto.response.goal.CreateChallengeResponseDto;
import com.project.moabuja.model.*;
import org.springframework.http.ResponseEntity;

public interface ChallengeGoalService {

    ResponseEntity<GroupCreateResponse> save(CreateChallengeRequestDto challengeRequestDto, Member currentMember);

    ResponseEntity<ChallengeResponseDto> getChallengeInfo(Member currentMember);

    ResponseEntity<CreateChallengeResponseDto> getChallengeMemberCandidates(Member currentMember);

    ResponseEntity<ChallengePostResponse> postChallenge(Member currentMember, GoalAlarmRequestDto goalAlarmRequestDto);

    ResponseEntity<ChallengeAcceptResponse> postChallengeAccept(Member currentMember, Long alarmId);

    ResponseEntity<ChallengeRefuseResponse> postChallengeRefuse(Member currentMember, Long alarmId);

    ResponseEntity<ChallengeExitResponse> exitChallenge(Member currentMember);

    ResponseEntity<ChallengeExitResponse> exitWaitingChallenge(Long id);
}
