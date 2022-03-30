package com.project.moabuja.service;

import com.project.moabuja.domain.member.Member;
import com.project.moabuja.dto.Res;
import com.project.moabuja.dto.request.alarm.GoalAlarmRequestDto;
import com.project.moabuja.dto.request.goal.CreateChallengeRequestDto;
import com.project.moabuja.dto.response.goal.ChallengeResponseDto;
import com.project.moabuja.dto.response.goal.CreateChallengeResponseDto;
import org.springframework.http.ResponseEntity;

public interface ChallengeGoalService {

    ResponseEntity<Res.GroupCreateResponse> save(CreateChallengeRequestDto challengeRequestDto, Member currentMember);

    ResponseEntity<ChallengeResponseDto> getChallengeInfo(Member currentMember);

    ResponseEntity<CreateChallengeResponseDto> getChallengeMemberCandidates(Member currentMember);

    ResponseEntity<Res.ChallengePostResponse> postChallenge(Member currentMember, GoalAlarmRequestDto goalAlarmRequestDto);

    ResponseEntity<Res.ChallengeAcceptResponse> postChallengeAccept(Member currentMember, Long alarmId);

    ResponseEntity<Res.ChallengeRefuseResponse> postChallengeRefuse(Member currentMember, Long alarmId);

    ResponseEntity<Res.ChallengeExitResponse> exitChallenge(Member currentMember);

    ResponseEntity<Res.ChallengeExitResponse> exitWaitingChallenge(Long id);
}
