package com.project.moabuja.controller;

import com.project.moabuja.domain.member.Member;
import com.project.moabuja.dto.response.alarm.FriendAlarmResponseDto;
import com.project.moabuja.dto.response.alarm.GoalAlarmResponseDto;
import com.project.moabuja.model.AlarmDeleteResponse;
import com.project.moabuja.security.userdetails.UserDetailsImpl;
import com.project.moabuja.service.AlarmService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AlarmController {

    private final AlarmService alarmService;

    @ApiOperation(value = "친구 알람 페이지")
    @GetMapping("/alarm/friend")
    public ResponseEntity<List<FriendAlarmResponseDto>> getFriendAlarm(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Member currentMember = userDetails.getMember();
        return alarmService.getFriendAlarm(currentMember);
    }

    @ApiOperation(value = "같이해부자 알람 페이지")
    @GetMapping("/alarm/group")
    public ResponseEntity<List<GoalAlarmResponseDto>> getGroupGoalAlarm(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Member currentMember = userDetails.getMember();
        return alarmService.getGroupGoalAlarm(currentMember);
    }

    @ApiOperation(value = "도전해부자 알람 페이지")
    @GetMapping("/alarm/challenge")
    public ResponseEntity<List<GoalAlarmResponseDto>> getChallengeGoalAlarm(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Member currentMember = userDetails.getMember();
        return alarmService.getChallengeGoalAlarm(currentMember);
    }

    @ApiOperation(value = "알람 삭제")
    @DeleteMapping("/alarm/{id}")
    public ResponseEntity<AlarmDeleteResponse> deleteAlarm(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                           @PathVariable Long alarmId) {
        Member currentMember = userDetails.getMember();
        return alarmService.deleteAlarm(currentMember, alarmId);
    }
}
