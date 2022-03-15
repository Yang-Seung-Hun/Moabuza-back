package com.project.moabuja.controller;

import com.project.moabuja.domain.member.Member;
import com.project.moabuja.dto.request.goal.CreateChallengeRequestDto;
import com.project.moabuja.dto.response.goal.ChallengeResponseDto;
import com.project.moabuja.dto.response.goal.CreateChallengeResponseDto;
import com.project.moabuja.security.userdetails.UserDetailsImpl;
import com.project.moabuja.service.ChallengeGoalService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ChallengeController {

    private final ChallengeGoalService challengeGoalService;

    @GetMapping("/money/challenge")
    public ChallengeResponseDto getMoneyChallenge(@AuthenticationPrincipal UserDetailsImpl userDetails){

        Member currentUser = userDetails.getMember();
        return challengeGoalService.getChallengeInfo(currentUser);
    }

    @GetMapping("/money/challenge/createChallenge")
    public CreateChallengeResponseDto GetCreateChallenge(@AuthenticationPrincipal UserDetailsImpl userDetails){

        Member currentUser = userDetails.getMember();
        return challengeGoalService.getChallengeMemberCandidates(currentUser);
    }

    @PostMapping("/money/challenge/createChallenge")
    public void PostCreateChallenge(@RequestBody CreateChallengeRequestDto createChallengeRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){

        Member currentUser = userDetails.getMember();
        challengeGoalService.save(createChallengeRequestDto, currentUser);
    }

    @DeleteMapping("/money/challenge/exitchallenge/{id}")
    public void exitChallenge(@PathVariable Long id){
        challengeGoalService.exitChallenge(id);
    }

}
