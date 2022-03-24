package com.project.moabuja.service;

import com.project.moabuja.domain.alarm.Alarm;
import com.project.moabuja.domain.friend.Friend;
import com.project.moabuja.domain.member.Member;
import com.project.moabuja.dto.request.alarm.FriendAlarmDto;
import com.project.moabuja.dto.request.alarm.AlarmGoalRequestDto;
import com.project.moabuja.dto.response.alarm.FriendAlarmResponseDto;
import com.project.moabuja.repository.AlarmRepository;
import com.project.moabuja.repository.FriendRepository;
import com.project.moabuja.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.project.moabuja.domain.alarm.AlarmType.FRIEND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AlarmServiceImpl implements AlarmService {

    private final AlarmRepository alarmRepository;
    private final MemberRepository memberRepository;
    private final FriendRepository friendRepository;
    private final FriendService friendService;

    @Transactional
    @Override
    public ResponseEntity<String> postFriendAlarm(FriendAlarmDto friendAlarmDto, Member currentMember) {
        Optional<Member> friend = Optional.ofNullable(memberRepository.findMemberByNickname(friendAlarmDto.getFriendNickname()).orElseThrow(
                () -> new UsernameNotFoundException("해당 닉네임의 사용자가 존재하지 않습니다.")
        ));
        Optional<Friend> isFriend = Optional.ofNullable(friendRepository.findByMemberAndFriend(currentMember, friend.get()));
        if (isFriend.isPresent()) {throw new IllegalArgumentException("해당 닉네임의 사용자와 친구 상태입니다.");}
        friendAlarmDto.insertMember(friend.get());
        alarmRepository.save(FriendAlarmDto.friendToEntity(friendAlarmDto, currentMember.getNickname()));

        return ResponseEntity.ok().body("친구 요청 알람 보내기 완료");
    }

    @Override
    public ResponseEntity<List<FriendAlarmResponseDto>> getFriendAlarm(Member currentMember) {
        List<Alarm> alarms = alarmRepository.findAlarmsByMemberAndAlarmTypeOrderByCreatedAtDesc(currentMember, FRIEND);
        List<FriendAlarmResponseDto> friendAlarmList = alarms.stream().map(alarm -> {
            return new FriendAlarmResponseDto(alarm.getId(), alarm.getAlarmDetailType(), alarm.getFriendNickname());
        }).collect(Collectors.toList());

        return ResponseEntity.ok().body(friendAlarmList);
    }

    @Transactional
    @Override
    public ResponseEntity<String> postFriendAcceptAlarm(Member currentMember, String friendNickname) {
        friendService.save(currentMember, friendNickname);

        Alarm alarm = alarmRepository.findByMemberAndFriendNickname(currentMember, friendNickname);
        alarmRepository.delete(alarm);

        return ResponseEntity.ok().body("친구 요청을 수락하였습니다.");
    }

    @Transactional
    @Override
    public ResponseEntity<String> postFriendRefuseAlarm(Member currentMember, String friendNickname) {
        Alarm alarm = alarmRepository.findByMemberAndFriendNickname(currentMember, friendNickname);
        alarmRepository.delete(alarm);

        return ResponseEntity.ok().body("친구 요청을 거절하였습니다.");
    }


    @Transactional
    @Override
    public ResponseEntity<String> postAlarmGoal(AlarmGoalRequestDto alarmGoalRequestDto, Member currentMember) {
        Optional<Member> current = memberRepository.findById(currentMember.getId());
        Optional<Member> friend = memberRepository.findById(alarmGoalRequestDto.getToFriend().getId());
        alarmGoalRequestDto.insertMember(friend.get());
        alarmRepository.save(AlarmGoalRequestDto.goalToEntity(alarmGoalRequestDto, current.get().getNickname()));

        return ResponseEntity.ok().body("해부자 알람 보내기 완료");
    }

}
