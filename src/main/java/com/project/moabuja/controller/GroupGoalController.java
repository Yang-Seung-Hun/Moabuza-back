package com.project.moabuja.controller;

import com.project.moabuja.domain.member.Member;
import com.project.moabuja.dto.request.goal.CreateGroupRequestDto;
import com.project.moabuja.dto.response.goal.GroupResponseDto;
import com.project.moabuja.security.userdetails.UserDetailsImpl;
import com.project.moabuja.service.GroupGoalService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class GroupGoalController {

    private final GroupGoalService groupGoalService;

    @GetMapping("/money/group")
    public GroupResponseDto getMoneyGroup(@AuthenticationPrincipal UserDetailsImpl userDetails){
        Member currentUser = userDetails.getMember();
        return groupGoalService.getGroupInfo(currentUser);
    }

    @GetMapping("/money/group/creategroup")
    public void getCreateGroup(@AuthenticationPrincipal UserDetailsImpl userDetails){
        Member currentUser = userDetails.getMember();
        groupGoalService.getGroupMemberCandidates(currentUser);
    }

    @PostMapping("/money/group/creategroup")
    public void postCreateGroup(@RequestBody CreateGroupRequestDto createGroupRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        Member currentUser = userDetails.getMember();
        groupGoalService.save(createGroupRequestDto,currentUser);
    }

    @GetMapping("/money/group/exitgroup/{id}")
    public void exitGroup(@PathVariable Long id){
        groupGoalService.exitChallenge(id);
    }

}
