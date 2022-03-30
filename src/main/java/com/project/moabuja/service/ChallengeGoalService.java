package com.project.moabuja.service;

import com.project.moabuja.domain.member.Member;
import com.project.moabuja.dto.Res;
import com.project.moabuja.dto.ResponseMsg;
import com.project.moabuja.dto.request.alarm.GoalAlarmRequestDto;
import com.project.moabuja.dto.request.goal.CreateChallengeRequestDto;
import com.project.moabuja.dto.response.goal.ChallengeResponseDto;
import com.project.moabuja.dto.response.goal.CreateChallengeResponseDto;
import org.springframework.http.ResponseEntity;

public interface ChallengeGoalService {

    ResponseEntity<Res> save(CreateChallengeRequestDto challengeRequestDto, Member currentMember);

    ResponseEntity<ChallengeResponseDto> getChallengeInfo(Member currentMember);

    ResponseEntity<CreateChallengeResponseDto> getChallengeMemberCandidates(Member currentMember);

    ResponseEntity<Res> postChallenge(Member currentMember, GoalAlarmRequestDto goalAlarmRequestDto);

    ResponseEntity<ResponseMsg> postChallengeAccept(Member currentMember, Long alarmId);

    ResponseEntity<ResponseMsg> postChallengeRefuse(Member currentMember, Long alarmId);

    ResponseEntity<ResponseMsg> exitChallenge(Member currentMember);

    ResponseEntity<ResponseMsg> exitWaitingChallenge(Long id);
}
