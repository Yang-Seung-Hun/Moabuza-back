package com.project.moabuja.service;

import com.project.moabuja.domain.alarm.Alarm;
import com.project.moabuja.domain.member.Member;
import com.project.moabuja.dto.Msg;
import com.project.moabuja.dto.response.alarm.FriendAlarmResponseDto;
import com.project.moabuja.dto.response.alarm.GoalAlarmResponseDto;
import com.project.moabuja.exception.ErrorException;
import com.project.moabuja.repository.AlarmRepository;
import com.project.moabuja.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.project.moabuja.domain.alarm.AlarmType.*;
import static com.project.moabuja.dto.ResponseMsg.AlarmDelete;
import static com.project.moabuja.exception.ErrorCode.*;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AlarmServiceImpl implements AlarmService {

    private final AlarmRepository alarmRepository;
    private final MemberRepository memberRepository;

    @Override
    public ResponseEntity<List<FriendAlarmResponseDto>> getFriendAlarm(Member currentMember) {
        List<Alarm> alarms = alarmRepository.findAlarmsByMemberAndAlarmTypeOrderByCreatedAtDesc(currentMember, FRIEND);
        List<FriendAlarmResponseDto> friendAlarmList = alarms.stream()
                .map(FriendAlarmResponseDto::of)
                .collect(Collectors.toList());

        return new ResponseEntity<>(friendAlarmList, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<GoalAlarmResponseDto>> getGroupGoalAlarm(Member currentMember) {
        List<Alarm> alarms = alarmRepository.findAlarmsByMemberAndAlarmTypeOrderByCreatedAtDesc(currentMember, GROUP);
        List<GoalAlarmResponseDto> groupAlarmList = alarms.stream()
                .map(GoalAlarmResponseDto::of)
                .collect(Collectors.toList());
        return new ResponseEntity<>(groupAlarmList, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<GoalAlarmResponseDto>> getChallengeGoalAlarm(Member currentMember) {
        List<Alarm> alarms = alarmRepository.findAlarmsByMemberAndAlarmTypeOrderByCreatedAtDesc(currentMember, CHALLENGE);
        List<GoalAlarmResponseDto> groupAlarmList = alarms.stream()
                .map(GoalAlarmResponseDto::of)
                .collect(Collectors.toList());

        return new ResponseEntity<>(groupAlarmList, HttpStatus.OK);
    }

    @Transactional
    @Override
    public ResponseEntity<Msg> deleteAlarm(Member currentMember, Long alarmId) {

        Member member = Optional.of(memberRepository.findById(currentMember.getId())).get().orElseThrow(() -> new ErrorException(MEMBER_NOT_FOUND));
        Alarm alarm = Optional.of(alarmRepository.findById(alarmId)).get().orElseThrow(() -> new ErrorException(ALARM_NOT_EXIST));

        if (Objects.equals(member, alarm.getMember())) {
            alarmRepository.deleteById(alarmId);
            return new ResponseEntity<>(new Msg(AlarmDelete.getMsg()), HttpStatus.OK);
        } throw new ErrorException(ALARM_MEMBER_NOT_MATCH);
    }
}