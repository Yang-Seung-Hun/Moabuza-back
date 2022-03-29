package com.project.moabuja.service;

import com.project.moabuja.domain.alarm.Alarm;
import com.project.moabuja.domain.member.Member;
import com.project.moabuja.dto.response.alarm.FriendAlarmResponseDto;
import com.project.moabuja.dto.response.alarm.GoalAlarmResponseDto;
import com.project.moabuja.exception.ErrorException;
import com.project.moabuja.repository.AlarmRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.project.moabuja.domain.alarm.AlarmType.*;
import static com.project.moabuja.exception.ErrorCode.*;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AlarmServiceImpl implements AlarmService {

    private final AlarmRepository alarmRepository;

    @Override
    public ResponseEntity<List<FriendAlarmResponseDto>> getFriendAlarm(Member currentMember) {
        List<Alarm> alarms = alarmRepository.findAlarmsByMemberAndAlarmTypeOrderByCreatedAtDesc(currentMember, FRIEND);
        List<FriendAlarmResponseDto> friendAlarmList = alarms.stream()
                .map(FriendAlarmResponseDto::of)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(friendAlarmList);
    }

    @Override
    public ResponseEntity<List<GoalAlarmResponseDto>> getGroupGoalAlarm(Member currentMember) {
        List<Alarm> alarms = alarmRepository.findAlarmsByMemberAndAlarmTypeOrderByCreatedAtDesc(currentMember, GROUP);
        List<GoalAlarmResponseDto> groupAlarmList = alarms.stream()
                .map(GoalAlarmResponseDto::of)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(groupAlarmList);
    }

    @Override
    public ResponseEntity<List<GoalAlarmResponseDto>> getChallengeGoalAlarm(Member currentMember) {
        List<Alarm> alarms = alarmRepository.findAlarmsByMemberAndAlarmTypeOrderByCreatedAtDesc(currentMember, CHALLENGE);
        List<GoalAlarmResponseDto> groupAlarmList = alarms.stream()
                .map(GoalAlarmResponseDto::of)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(groupAlarmList);
    }

    @Transactional
    @Override
    public ResponseEntity<String> deleteAlarm(Member currentMember, Long alarmId) {
        Alarm alarm = Optional
                .of(alarmRepository.findById(alarmId)).get()
                .orElseThrow(() -> new ErrorException(ALARM_NOT_EXIST));

        if (Objects.equals(currentMember, alarm.getMember())) {
            alarmRepository.deleteById(alarmId);
            return ResponseEntity.ok().body("알람이 삭제되었습니다.");
        } throw new ErrorException(ALARM_MEMBER_NOT_MATCH);
    }

}