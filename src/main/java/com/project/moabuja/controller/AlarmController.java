package com.project.moabuja.controller;

import com.project.moabuja.domain.member.Member;
import com.project.moabuja.dto.request.alarm.FriendAlarmDto;
import com.project.moabuja.dto.request.alarm.GoalAlarmRequestDto;
import com.project.moabuja.dto.response.alarm.FriendAlarmResponseDto;
import com.project.moabuja.dto.response.alarm.GoalAlarmResponseDto;
import com.project.moabuja.security.userdetails.UserDetailsImpl;
import com.project.moabuja.service.AlarmService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @ApiOperation(value = "친구 요청")
    @PostMapping("/alarm/friend")
    public ResponseEntity<String> postFriendAlarm(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                          @RequestBody FriendAlarmDto friendAlarmDto) {
        Member currentMember = userDetails.getMember();
        return alarmService.postFriendAlarm(friendAlarmDto, currentMember);
    }

    @ApiOperation(value = "친구 수락 알람")
    @PostMapping("/alarm/friend/accept/{friendNickname}")
    public ResponseEntity<String> postFriendAcceptAlarm(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                        @RequestParam String friendNickname) {
        Member currentMember = userDetails.getMember();
        return alarmService.postFriendAcceptAlarm(currentMember, friendNickname);
    }

    @ApiOperation(value = "친구 거절 알람")
    @PostMapping("/alarm/friend/refuse/{friendNickname}")
    public ResponseEntity<String> postFriendRefuseAlarm(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                        @RequestParam String friendNickname) {
        Member currentMember = userDetails.getMember();
        return alarmService.postFriendRefuseAlarm(currentMember, friendNickname);
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

    @ApiOperation(value = "해부자 요청")
    @PostMapping("/alarm/goal")
    public ResponseEntity<String> postGoalAlarm(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                @RequestBody GoalAlarmRequestDto goalAlarmRequestDto) {
        Member currentMember = userDetails.getMember();
        return alarmService.postGoalAlarm(currentMember, goalAlarmRequestDto);
    }

    @ApiOperation(value = "해부자 수락")
    @PostMapping("/alarm/goal/accept/{alarmId}")
    public ResponseEntity<String> postGoalAcceptAlarm(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                      @PathVariable Long alarmId) {
        Member currentMember = userDetails.getMember();
        return alarmService.postGoalAcceptAlarm(currentMember, alarmId);
    }

    @ApiOperation(value = "해부자 거절")
    @PostMapping("/alarm/goal/refuse/{alarmId}")
    public ResponseEntity<String> postGoalRefuseAlarm(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                      @PathVariable Long alarmId) {
        Member currentMember = userDetails.getMember();
        return alarmService.postGoalRefuseAlarm(currentMember, alarmId);
    }

    @ApiOperation(value = "알람 삭제")
    @DeleteMapping("/alarm/delete/{alarmId}")
    public ResponseEntity<String> deleteAlarm(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                              @PathVariable Long alarmId) {
        Member currentMember = userDetails.getMember();
        return alarmService.deleteAlarm(currentMember, alarmId);
    }
}
