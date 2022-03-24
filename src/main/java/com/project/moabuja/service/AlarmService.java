package com.project.moabuja.service;

import com.project.moabuja.domain.member.Member;
import com.project.moabuja.dto.request.alarm.FriendAlarmDto;
import com.project.moabuja.dto.request.alarm.AlarmGoalRequestDto;
import com.project.moabuja.dto.response.alarm.FriendAlarmResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AlarmService {

    public ResponseEntity<String> postFriendAlarm(FriendAlarmDto friendAlarmDto, Member currentMember);

    public ResponseEntity<List<FriendAlarmResponseDto>> getFriendAlarm(Member currentMember);

    public ResponseEntity<String> postFriendAcceptAlarm(Member currentMember, String friendNickname);

    public ResponseEntity<String> postFriendRefuseAlarm(Member currentMember, String friendNickname);

    public ResponseEntity<String> postAlarmGoal(AlarmGoalRequestDto alarmGoalRequestDto, Member currentMember);
}
