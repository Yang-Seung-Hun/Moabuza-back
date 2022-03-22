package com.project.moabuja.service;

import com.project.moabuja.domain.member.Member;
import com.project.moabuja.dto.request.alarm.AlarmFriendRequestDto;
import com.project.moabuja.dto.request.alarm.AlarmGoalRequestDto;
import com.project.moabuja.dto.response.alarm.AlarmResponseDto;
import org.springframework.http.ResponseEntity;

public interface AlarmService {

    public ResponseEntity<String> postAlarmFriend(AlarmFriendRequestDto alarmFriendRequestDto, Member currentMember);

    public ResponseEntity<String> postAlarmGoal(AlarmGoalRequestDto alarmGoalRequestDto, Member currentMember);
}
