package com.project.moabuja.service;

import com.project.moabuja.domain.alarm.Alarm;
import com.project.moabuja.domain.alarm.AlarmDetailType;
import com.project.moabuja.domain.friend.Friend;
import com.project.moabuja.domain.goal.GoalType;
import com.project.moabuja.domain.goal.MemberWaitingGoal;
import com.project.moabuja.domain.goal.WaitingGoal;
import com.project.moabuja.domain.member.Member;
import com.project.moabuja.dto.request.alarm.FriendAlarmDto;
import com.project.moabuja.dto.request.alarm.GoalAlarmRequestDto;
import com.project.moabuja.dto.request.alarm.GoalAlarmSaveDto;
import com.project.moabuja.dto.request.friend.FriendRequestDto;
import com.project.moabuja.dto.request.goal.CreateChallengeRequestDto;
import com.project.moabuja.dto.request.goal.CreateGroupRequestDto;
import com.project.moabuja.dto.request.goal.WaitingGoalSaveDto;
import com.project.moabuja.dto.response.alarm.FriendAlarmResponseDto;
import com.project.moabuja.dto.response.alarm.GoalAlarmResponseDto;
import com.project.moabuja.exception.exceptionClass.AlarmErrorException;
import com.project.moabuja.exception.exceptionClass.MemberNotFoundException;
import com.project.moabuja.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.project.moabuja.domain.alarm.AlarmType.*;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AlarmServiceImpl implements AlarmService {

    private final AlarmRepository alarmRepository;
    private final MemberRepository memberRepository;
    private final FriendRepository friendRepository;
    private final WaitingGoalRepository waitingGoalRepository;
    private final MemberWaitingGoalRepository memberWaitingGoalRepository;
    private final FriendService friendService;
    private final GroupGoalService groupGoalService;
    private final ChallengeGoalService challengeGoalService;

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
        if (alarmRepository.findById(alarmId).isEmpty()) { throw new AlarmErrorException("해당 알람이 없습니다."); }

        Member member = alarmRepository.findById(alarmId).get().getMember();

        if (member.getClass().isInstance(currentMember)) {
            alarmRepository.deleteById(alarmId);

            return ResponseEntity.ok().body("알람이 삭제되었습니다.");
        }

        throw new AlarmErrorException("알람에 해당하는 사용자가 아닙니다.");
    }

}
