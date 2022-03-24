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
import com.project.moabuja.dto.request.goal.WaitingGoalSaveDto;
import com.project.moabuja.dto.response.alarm.FriendAlarmResponseDto;
import com.project.moabuja.dto.response.alarm.GoalAlarmResponseDto;
import com.project.moabuja.exception.exceptionClass.AlarmErrorException;
import com.project.moabuja.exception.exceptionClass.MemberNotFoundException;
import com.project.moabuja.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.project.moabuja.domain.alarm.AlarmType.FRIEND;
import static com.project.moabuja.domain.alarm.AlarmType.GROUP;

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

    @Override
    public ResponseEntity<List<FriendAlarmResponseDto>> getFriendAlarm(Member currentMember) {
        List<Alarm> alarms = alarmRepository.findAlarmsByMemberAndAlarmTypeOrderByCreatedAtDesc(currentMember, FRIEND);
        List<FriendAlarmResponseDto> friendAlarmList = alarms.stream()
                .map(FriendAlarmResponseDto::of)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(friendAlarmList);
    }

    @Transactional
    @Override
    public ResponseEntity<String> postFriendAlarm(FriendAlarmDto friendAlarmDto, Member currentMember) {
        Optional<Member> friend = memberRepository.findMemberByNickname(friendAlarmDto.getFriendNickname());
        if (friend.isPresent()) {
            Optional<Friend> isFriend = Optional.ofNullable(friendRepository.findByMemberAndFriend(currentMember, friend.get()));
            if (isFriend.isPresent()) { throw new IllegalArgumentException("해당 닉네임의 사용자와 친구 상태입니다."); }
            alarmRepository.save(FriendAlarmDto.friendToEntity(AlarmDetailType.request, friend.get(), currentMember.getNickname()));
        } else { throw new MemberNotFoundException("해당 사용자는 존재하지 않습니다."); }

        return ResponseEntity.ok().body("친구 요청 알람 보내기 완료");
    }

    @Transactional
    @Override
    public ResponseEntity<String> postFriendAcceptAlarm(Member currentMember, String friendNickname) {
        friendService.save(currentMember, friendNickname);

        Optional<Member> toFriend = memberRepository.findMemberByNickname(friendNickname);
        if (toFriend.isPresent()) {
            alarmRepository.save(FriendAlarmDto.friendToEntity(AlarmDetailType.accept, toFriend.get(), currentMember.getNickname()));
        } else { throw new MemberNotFoundException("해당 사용자는 존재하지 않습니다."); }

        Alarm alarm = alarmRepository.findByMemberAndFriendNickname(currentMember, friendNickname);
        alarmRepository.delete(alarm);

        return ResponseEntity.ok().body("친구 요청을 수락하였습니다.");
    }

    @Transactional
    @Override
    public ResponseEntity<String> postFriendRefuseAlarm(Member currentMember, String friendNickname) {
        Optional<Member> toFriend = memberRepository.findMemberByNickname(friendNickname);
        if (toFriend.isPresent()) {
            alarmRepository.save(FriendAlarmDto.friendToEntity(AlarmDetailType.refuse, toFriend.get(), currentMember.getNickname()));
        } else { throw new MemberNotFoundException("해당 사용자는 존재하지 않습니다."); }

        Alarm alarm = alarmRepository.findByMemberAndFriendNickname(currentMember, friendNickname);
        alarmRepository.delete(alarm);

        return ResponseEntity.ok().body("친구 요청을 거절하였습니다.");
    }

    @Override
    public ResponseEntity<List<GoalAlarmResponseDto>> getGroupGoalAlarm(Member currentMember) {
        List<Alarm> alarms = alarmRepository.findAlarmsByMemberAndAlarmTypeOrderByCreatedAtDesc(currentMember, GROUP);
        List<GoalAlarmResponseDto> groupAlarmList = alarms.stream()
                .map(GoalAlarmResponseDto::of)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(groupAlarmList);
    }

    @Transactional
    @Override
    public ResponseEntity<String> postGroupGoalAlarm(Member currentMember, GoalAlarmRequestDto goalAlarmRequestDto) {
        WaitingGoal waitingGoal = waitingGoalRepository.save(WaitingGoalSaveDto.toEntity(goalAlarmRequestDto.getGoalName(), goalAlarmRequestDto.getGoalAmount(), GoalType.GROUP));
        MemberWaitingGoal currentMemberWaitingGoal = new MemberWaitingGoal(currentMember, waitingGoal, false);
        memberWaitingGoalRepository.save(currentMemberWaitingGoal);

        for (String friendNickname : goalAlarmRequestDto.getFriendNickname()) {
            Optional<Member> member = memberRepository.findMemberByNickname(friendNickname);
            if (member.isPresent()) {
                MemberWaitingGoal memberWaitingGoal = new MemberWaitingGoal(member.get(), waitingGoal, false);
                memberWaitingGoalRepository.save(memberWaitingGoal);

                alarmRepository.save(GoalAlarmSaveDto.goalToEntity(goalAlarmRequestDto, waitingGoal.getId(), GROUP, AlarmDetailType.invite, currentMember.getNickname(), member.get()));
            } else { throw new MemberNotFoundException("해당 사용자는 존재하지 않습니다."); }
        }

        return ResponseEntity.ok().body("같이해부자 요청 완료");
    }

    @Transactional
    @Override
    public ResponseEntity<String> postGoalAcceptAlarm(Member currentMember, Long alarmId) {
        Alarm alarm = alarmRepository.findAlarmById(alarmId);
        WaitingGoal waitingGoal = waitingGoalRepository.findWaitingGoalById(alarm.getWaitingGoalId());
        MemberWaitingGoal currentMemberWaitingGoal = memberWaitingGoalRepository.findMemberWaitingGoalByMemberAndWaitingGoal(currentMember, waitingGoal);
        currentMemberWaitingGoal.changeIsAcceptedGoal(currentMemberWaitingGoal);

        // 전체 수락 전
        List<MemberWaitingGoal> friends = memberWaitingGoalRepository.findMemberWaitingGoalsByWaitingGoal(waitingGoal);
        for (MemberWaitingGoal memberWaitingGoal : friends) {
            memberWaitingGoal.isAcceptedGoal();
        }

        return ResponseEntity.ok().body("해부자 수락 완료");
    }

    @Transactional
    @Override
    public ResponseEntity<String> deleteAlarm(Member currentMember, Long alarmId) {
        if (alarmRepository.findById(alarmId).isEmpty()) { throw new AlarmErrorException("해당 알람이 없습니다."); }
        if (! alarmRepository.findById(alarmId).get().getMember().equals(currentMember)) { throw new AlarmErrorException("알람에 해당하는 사용자가 아닙니다."); }
        alarmRepository.deleteById(alarmId);

        return ResponseEntity.ok().body("알람이 삭제되었습니다.");
    }

}
