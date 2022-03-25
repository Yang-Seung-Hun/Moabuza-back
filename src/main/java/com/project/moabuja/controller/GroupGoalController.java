package com.project.moabuja.controller;

import com.project.moabuja.domain.member.Member;
import com.project.moabuja.dto.request.goal.CreateGroupRequestDto;
import com.project.moabuja.dto.response.goal.CreateGroupResponseDto;
import com.project.moabuja.dto.response.goal.GroupResponseDto;
import com.project.moabuja.security.userdetails.UserDetailsImpl;
import com.project.moabuja.service.GroupGoalService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class GroupGoalController {

    private final GroupGoalService groupGoalService;

    @ApiOperation(value = "같이해부자 페이지")
    @GetMapping("/money/group")
    public ResponseEntity<GroupResponseDto> getMoneyGroup(@AuthenticationPrincipal UserDetailsImpl userDetails){
        Member currentMember = userDetails.getMember();
        return groupGoalService.getGroupInfo(currentMember);
    }

    @ApiOperation(value = "같이해부자 생성 페이지")
    @GetMapping("/money/group/creategroup")
    public ResponseEntity<CreateGroupResponseDto> getCreateGroup(@AuthenticationPrincipal UserDetailsImpl userDetails){
        Member currentMember = userDetails.getMember();
        return groupGoalService.getGroupMemberCandidates(currentMember);
    }

    @ApiOperation(value = "같이해부자 생성")
    @PostMapping("/money/group/creategroup")
    public ResponseEntity<String> postCreateGroup(@RequestBody CreateGroupRequestDto createGroupRequestDto,
                                                  @AuthenticationPrincipal UserDetailsImpl userDetails){
        Member currentMember = userDetails.getMember();
        return groupGoalService.save(createGroupRequestDto,currentMember);
    }

    @ApiOperation(value = "같이해부자 나가기")
    @DeleteMapping("/money/group/exitgroup/{id}")
    public ResponseEntity<String> exitGroup(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id){
        Member currentMember = userDetails.getMember();
        return groupGoalService.exitGroup(currentMember, id);
    }

}
