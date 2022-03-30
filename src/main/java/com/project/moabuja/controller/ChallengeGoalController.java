package com.project.moabuja.controller;

import com.project.moabuja.domain.member.Member;
import com.project.moabuja.dto.request.alarm.GoalAlarmRequestDto;
import com.project.moabuja.dto.response.goal.ChallengeResponseDto;
import com.project.moabuja.dto.response.goal.CreateChallengeResponseDto;
import com.project.moabuja.model.ChallengeAcceptResponse;
import com.project.moabuja.model.ChallengeExitResponse;
import com.project.moabuja.model.ChallengePostResponse;
import com.project.moabuja.model.ChallengeRefuseResponse;
import com.project.moabuja.security.userdetails.UserDetailsImpl;
import com.project.moabuja.service.ChallengeGoalService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChallengeGoalController {

    private final ChallengeGoalService challengeGoalService;

    @ApiOperation(value = "도전해부자 페이지")
    @GetMapping("/challenge")
    public ResponseEntity<ChallengeResponseDto> getMoneyChallenge(@AuthenticationPrincipal UserDetailsImpl userDetails){
        Member currentMember = userDetails.getMember();
        return challengeGoalService.getChallengeInfo(currentMember);
    }

    @ApiOperation(value = "같이 도전할 친구 목록")
    @GetMapping("/challenge/friend")
    public ResponseEntity<CreateChallengeResponseDto> getCreateChallenge(@AuthenticationPrincipal UserDetailsImpl userDetails){
        Member currentMember = userDetails.getMember();
        return challengeGoalService.getChallengeMemberCandidates(currentMember);
    }

    @ApiOperation(value = "도전해부자 요청")
    @PostMapping("/challenge")
    public ResponseEntity<ChallengePostResponse> postChallenge(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                               @RequestBody GoalAlarmRequestDto goalAlarmRequestDto) {
        Member currentMember = userDetails.getMember();
        return challengeGoalService.postChallenge(currentMember, goalAlarmRequestDto);
    }

    @ApiOperation(value = "도전해부자 수락")
    @PostMapping("/challenge/{id}/accept")
    public ResponseEntity<ChallengeAcceptResponse> postChallengeAccept(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                       @PathVariable Long alarmId) {
        Member currentMember = userDetails.getMember();
        return challengeGoalService.postChallengeAccept(currentMember, alarmId);
    }

    @ApiOperation(value = "도전해부자 거절")
    @PostMapping("/challenge/{id}/refuse")
    public ResponseEntity<ChallengeRefuseResponse> postChallengeRefuse(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                       @PathVariable Long alarmId) {
        Member currentMember = userDetails.getMember();
        return challengeGoalService.postChallengeRefuse(currentMember, alarmId);
    }

    @ApiOperation(value = "도전해부자 나가기")
    @DeleteMapping("/challenge/doing")
    public ResponseEntity<ChallengeExitResponse> exitChallenge(@AuthenticationPrincipal UserDetailsImpl userDetails){
        Member currentMember = userDetails.getMember();
        return challengeGoalService.exitChallenge(currentMember);
    }

    @ApiOperation(value = "대기중인 도전해부자 나가기")
    @DeleteMapping("/challenge/{id}/waiting")
    public ResponseEntity<ChallengeExitResponse> exitWaitChallenge(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id){
        Member currentMember = userDetails.getMember();
        return challengeGoalService.exitWaitingChallenge(id);
    }

}
