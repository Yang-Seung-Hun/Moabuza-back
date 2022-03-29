package com.project.moabuja.service;

import com.project.moabuja.domain.member.Member;
import com.project.moabuja.dto.request.alarm.GoalAlarmRequestDto;
import com.project.moabuja.dto.request.goal.CreateChallengeRequestDto;
import com.project.moabuja.dto.response.goal.ChallengeResponseDto;
import com.project.moabuja.dto.response.goal.CreateChallengeResponseDto;
import com.project.moabuja.model.ChallengeAcceptResponse;
import com.project.moabuja.model.ChallengeExitResponse;
import com.project.moabuja.model.ChallengePostResponse;
import com.project.moabuja.model.ChallengeRefuseResponse;
import org.springframework.http.ResponseEntity;

public interface ChallengeGoalService {

    void save(CreateChallengeRequestDto challengeRequestDto, Member currentMember);

    ResponseEntity<ChallengeResponseDto> getChallengeInfo(Member currentMember);

    ResponseEntity<CreateChallengeResponseDto> getChallengeMemberCandidates(Member currentMember);

    ResponseEntity<ChallengePostResponse> postChallenge(Member currentMember, GoalAlarmRequestDto goalAlarmRequestDto);

    ResponseEntity<ChallengeAcceptResponse> postChallengeAccept(Member currentMember, Long alarmId);

    ResponseEntity<ChallengeRefuseResponse> postChallengeRefuse(Member currentMember, Long alarmId);

    ResponseEntity<ChallengeExitResponse> exitChallenge(Member currentMember, Long id);

    ResponseEntity<ChallengeExitResponse> exitWaitingChallenge(Long id);
}
