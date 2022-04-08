package com.project.moabuja.service;

import com.project.moabuja.domain.member.Member;
import com.project.moabuja.dto.Msg;
import com.project.moabuja.dto.request.alarm.GoalAlarmRequestDto;
import com.project.moabuja.dto.request.goal.CreateChallengeRequestDto;
import com.project.moabuja.dto.response.goal.ChallengeResponseDto;
import com.project.moabuja.dto.response.goal.CreateChallengeResponseDto;
import org.springframework.http.ResponseEntity;

public interface ChallengeGoalService {

    ResponseEntity<Msg> save(CreateChallengeRequestDto challengeRequestDto, Member currentMember);

    ResponseEntity<ChallengeResponseDto> getChallengeInfo(Member currentMember);

    ResponseEntity<CreateChallengeResponseDto> getChallengeMemberCandidates(Member currentMember);

    ResponseEntity<Msg> postChallenge(Member currentMember, GoalAlarmRequestDto goalAlarmRequestDto);

    ResponseEntity<Msg> postChallengeAccept(Member currentMember, Long alarmId);

    ResponseEntity<Msg> postChallengeRefuse(Member currentMember, Long alarmId);

    ResponseEntity<Msg> exitChallenge(Member currentMember);

    ResponseEntity<Msg> exitWaitingChallenge(Member currentMember, Long id);
}
