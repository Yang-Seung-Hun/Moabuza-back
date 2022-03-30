package com.project.moabuja.controller;

import com.project.moabuja.domain.member.Member;
import com.project.moabuja.dto.Msg;
import com.project.moabuja.dto.request.alarm.GoalAlarmRequestDto;
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
    @GetMapping("/group")
    public ResponseEntity<GroupResponseDto> getMoneyGroup(@AuthenticationPrincipal UserDetailsImpl userDetails){
        Member currentMember = userDetails.getMember();
        return groupGoalService.getGroupInfo(currentMember);
    }

    @ApiOperation(value = "같이 도전할 친구 목록")
    @GetMapping("/group/friend")
    public ResponseEntity<CreateGroupResponseDto> getCreateGroup(@AuthenticationPrincipal UserDetailsImpl userDetails){
        Member currentMember = userDetails.getMember();
        return groupGoalService.getGroupMemberCandidates(currentMember);
    }

    @ApiOperation(value = "같이해부자 요청")
    @PostMapping("/group")
    public ResponseEntity<Msg> postGroup(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                         @RequestBody GoalAlarmRequestDto goalAlarmRequestDto) {
        Member currentMember = userDetails.getMember();
        return groupGoalService.postGroup(currentMember, goalAlarmRequestDto);
    }

    @ApiOperation(value = "같이해부자 수락")
    @PostMapping("/group/{id}/accept")
    public ResponseEntity<Msg> postGroupAccept(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                               @PathVariable Long alarmId) {
        Member currentMember = userDetails.getMember();
        return groupGoalService.postGroupAccept(currentMember, alarmId);
    }

    @ApiOperation(value = "같이해부자 거절")
    @PostMapping("/group/{id}/refuse")
    public ResponseEntity<Msg> postGroupRefuse(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                               @PathVariable Long alarmId) {
        Member currentMember = userDetails.getMember();
        return groupGoalService.postGroupRefuse(currentMember, alarmId);
    }

    @ApiOperation(value = "같이해부자 나가기")
    @DeleteMapping("/group/{id}/doing")
    public ResponseEntity<Msg> exitGroup(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id){
        Member currentMember = userDetails.getMember();
        return groupGoalService.exitGroup(currentMember, id);
    }

    @ApiOperation(value = "대기중인 같이해부자 나가기")
    @DeleteMapping("/group/{id}/waiting")
    public ResponseEntity<Msg> exitWaitGroup(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id){
        Member currentMember = userDetails.getMember();
        return groupGoalService.exitWaitingGroup(id);
    }

}
