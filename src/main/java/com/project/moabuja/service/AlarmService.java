package com.project.moabuja.service;

import com.project.moabuja.domain.member.Member;
import com.project.moabuja.dto.response.alarm.FriendAlarmResponseDto;
import com.project.moabuja.dto.response.alarm.GoalAlarmResponseDto;
import com.project.moabuja.dto.Res;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AlarmService {

    ResponseEntity<List<FriendAlarmResponseDto>> getFriendAlarm(Member currentMember);

    ResponseEntity<List<GoalAlarmResponseDto>> getGroupGoalAlarm(Member currentMember);

    ResponseEntity<List<GoalAlarmResponseDto>> getChallengeGoalAlarm(Member currentMember);

    ResponseEntity<Res.AlarmDeleteResponse> deleteAlarm(Member currentMember, Long alarmId);
}
