package com.project.moabuja.controller;

import com.project.moabuja.domain.member.Member;
import com.project.moabuja.dto.request.goal.CreateChallengeRequestDto;
import com.project.moabuja.dto.response.goal.ChallengeResponseDto;
import com.project.moabuja.dto.response.goal.CreateChallengeResponseDto;
import com.project.moabuja.security.userdetails.UserDetailsImpl;
import com.project.moabuja.service.ChallengeGoalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ChallengeController {

    private final ChallengeGoalService challengeGoalService;

    @GetMapping("/money/challenge")
    public ResponseEntity getMoneyChallenge(@AuthenticationPrincipal UserDetailsImpl userDetails){

        Member currentUser = userDetails.getMember();
        ChallengeResponseDto challengeResponseDto = challengeGoalService.getChallengeInfo(currentUser);

        return ResponseEntity.ok().body(challengeResponseDto);
    }

    @GetMapping("/money/challenge/createChallenge")
    public ResponseEntity getCreateChallenge(@AuthenticationPrincipal UserDetailsImpl userDetails){

        Member currentUser = userDetails.getMember();
        CreateChallengeResponseDto createChallengeResponseDto = challengeGoalService.getChallengeMemberCandidates(currentUser);

        return ResponseEntity.ok().body(createChallengeResponseDto);
    }

    @PostMapping("/money/challenge/createChallenge")
    public ResponseEntity postCreateChallenge(@RequestBody CreateChallengeRequestDto createChallengeRequestDto,
                                              @AuthenticationPrincipal UserDetailsImpl userDetails){

        Member currentUser = userDetails.getMember();
        challengeGoalService.save(createChallengeRequestDto, currentUser);

        return ResponseEntity.ok().body("도전해부자 생성 완료");
    }

    @DeleteMapping("/money/challenge/exitchallenge/{id}")
    public ResponseEntity exitChallenge(@PathVariable Long id){

        challengeGoalService.exitChallenge(id);

        return ResponseEntity.ok().body("도전해부자 나가기 완료");
    }

}
