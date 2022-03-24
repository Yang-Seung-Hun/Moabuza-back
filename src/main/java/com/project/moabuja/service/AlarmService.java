package com.project.moabuja.service;

import com.project.moabuja.domain.member.Member;
import com.project.moabuja.dto.request.alarm.FriendAlarmDto;
import com.project.moabuja.dto.request.alarm.GoalAlarmRequestDto;
import com.project.moabuja.dto.response.alarm.FriendAlarmResponseDto;
import com.project.moabuja.dto.response.alarm.GoalAlarmResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AlarmService {

    ResponseEntity<List<FriendAlarmResponseDto>> getFriendAlarm(Member currentMember);

    ResponseEntity<String> postFriendAlarm(FriendAlarmDto friendAlarmDto, Member currentMember);

    ResponseEntity<String> postFriendAcceptAlarm(Member currentMember, String friendNickname);

    ResponseEntity<String> postFriendRefuseAlarm(Member currentMember, String friendNickname);

    ResponseEntity<List<GoalAlarmResponseDto>> getGroupGoalAlarm(Member currentMember);

    ResponseEntity<List<GoalAlarmResponseDto>> getChallengeGoalAlarm(Member currentMember);

    ResponseEntity<String> postGoalAlarm(Member currentMember, GoalAlarmRequestDto goalAlarmRequestDto);

    ResponseEntity<String> postGoalAcceptAlarm(Member currentMember, Long alarmId);

    ResponseEntity<String> postGoalRefuseAlarm(Member currentMember, Long alarmId);

    ResponseEntity<String> deleteAlarm(Member currentMember, Long alarmId);
}
