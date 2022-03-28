package com.project.moabuja.service;

import com.project.moabuja.domain.goal.ChallengeGoal;
import com.project.moabuja.domain.member.Member;
import com.project.moabuja.dto.request.alarm.GoalAlarmRequestDto;
import com.project.moabuja.dto.request.goal.CreateChallengeRequestDto;
import com.project.moabuja.dto.response.goal.ChallengeResponseDto;
import com.project.moabuja.dto.response.goal.CreateChallengeResponseDto;
import okhttp3.Response;
import org.springframework.http.ResponseEntity;

public interface ChallengeGoalService {

    ResponseEntity<String> save(CreateChallengeRequestDto challengeRequestDto, Member currentMember);

    ResponseEntity<ChallengeResponseDto> getChallengeInfo(Member currentMember);

    ResponseEntity<CreateChallengeResponseDto> getChallengeMemberCandidates(Member currentMember);

    ResponseEntity<String> postChallenge(Member currentMember, GoalAlarmRequestDto goalAlarmRequestDto);

    ResponseEntity<String> postChallengeAccept(Member currentMember, Long alarmId);

    ResponseEntity<String> postChallengeRefuse(Member currentMember, Long alarmId);

    ResponseEntity<String> exitChallenge(Member currentMember, Long id);

    ResponseEntity<String> exitWaitingChallenge(Member currentMember, Long id);
}
