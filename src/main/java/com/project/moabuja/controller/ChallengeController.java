package com.project.moabuja.controller;

import com.project.moabuja.domain.member.Member;
import com.project.moabuja.dto.request.goal.CreateChallengeRequestDto;
import com.project.moabuja.dto.response.goal.ChallengeResponseDto;
import com.project.moabuja.dto.response.goal.CreateChallengeResponseDto;
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
public class ChallengeController {

    private final ChallengeGoalService challengeGoalService;

    @ApiOperation(value = "도전해부자 페이지")
    @GetMapping("/money/challenge")
    public ResponseEntity<ChallengeResponseDto> getMoneyChallenge(@AuthenticationPrincipal UserDetailsImpl userDetails){
        Member currentMember = userDetails.getMember();
        return challengeGoalService.getChallengeInfo(currentMember);
    }

    @ApiOperation(value = "도전해부자 생성 페이지")
    @GetMapping("/money/challenge/createChallenge")
    public ResponseEntity<CreateChallengeResponseDto> getCreateChallenge(@AuthenticationPrincipal UserDetailsImpl userDetails){
        Member currentMember = userDetails.getMember();
        return challengeGoalService.getChallengeMemberCandidates(currentMember);
    }

//    @ApiOperation(value = "도전해부자 생성")
//    @PostMapping("/money/challenge/createChallenge")
//    public ResponseEntity<String> postCreateChallenge(@RequestBody CreateChallengeRequestDto createChallengeRequestDto,
//                                                      @AuthenticationPrincipal UserDetailsImpl userDetails){
//        log.info("============== 도전해부자 생성 나옵니까? ===============");
//        Member currentMember = userDetails.getMember();
//        return challengeGoalService.save(createChallengeRequestDto, currentMember);
//    }

    @ApiOperation(value = "도전해부자 나가기")
    @DeleteMapping("/money/challenge/exitchallenge/{id}")
    public ResponseEntity<String> exitChallenge(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id){
        Member currentMember = userDetails.getMember();
        return challengeGoalService.exitChallenge(currentMember, id);
    }

    @ApiOperation(value = "도전해부자 나가기")
    @DeleteMapping("/money/challenge/exitWaitingChallenge/{id}")
    public ResponseEntity<String> exitWaitChallenge(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id){
        Member currentMember = userDetails.getMember();
        return challengeGoalService.exitWaitingChallenge(currentMember, id);
    }

}
