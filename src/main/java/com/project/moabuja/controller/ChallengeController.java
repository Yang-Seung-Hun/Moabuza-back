package com.project.moabuja.controller;

import com.project.moabuja.domain.member.Member;
import com.project.moabuja.dto.request.goal.CreateChallengeRequestDto;
import com.project.moabuja.dto.response.goal.ChallengeResponseDto;
import com.project.moabuja.dto.response.goal.CreateChallengeResponseDto;
import com.project.moabuja.security.userdetails.UserDetailsImpl;
import com.project.moabuja.service.ChallengeGoalService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ChallengeController {

    private final ChallengeGoalService challengeGoalService;

    @ApiOperation(value = "도전해부자 페이지")
    @GetMapping("/money/challenge")
    public ResponseEntity<ChallengeResponseDto> getMoneyChallenge(@AuthenticationPrincipal UserDetailsImpl userDetails){
        Member currentUser = userDetails.getMember();
        return challengeGoalService.getChallengeInfo(currentUser);
    }

    @ApiOperation(value = "도전해부자 생성 페이지")
    @GetMapping("/money/challenge/createChallenge")
    public ResponseEntity<CreateChallengeResponseDto> getCreateChallenge(@AuthenticationPrincipal UserDetailsImpl userDetails){
        Member currentUser = userDetails.getMember();
        return challengeGoalService.getChallengeMemberCandidates(currentUser);
    }

    @ApiOperation(value = "도전해부자 생성")
    @PostMapping("/money/challenge/createChallenge")
    public ResponseEntity<String> postCreateChallenge(@RequestBody CreateChallengeRequestDto createChallengeRequestDto,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetails){
        Member currentUser = userDetails.getMember();
        return challengeGoalService.save(createChallengeRequestDto, currentUser);
    }

    @ApiOperation(value = "도전해부자 삭제")
    @DeleteMapping("/money/challenge/exitchallenge/{id}")
    public ResponseEntity<String> exitChallenge(@PathVariable Long id){
        return challengeGoalService.exitChallenge(id);
    }

}
