package com.project.moabuja.service;

import com.project.moabuja.domain.member.Member;
import com.project.moabuja.dto.request.alarm.AlarmFriendRequestDto;
import com.project.moabuja.dto.request.alarm.AlarmGoalRequestDto;
import com.project.moabuja.dto.response.alarm.AlarmResponseDto;
import com.project.moabuja.repository.AlarmRepository;
import com.project.moabuja.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AlarmServiceImpl implements AlarmService {

    private final AlarmRepository alarmRepository;
    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public ResponseEntity<String> postAlarmFriend(AlarmFriendRequestDto alarmFriendRequestDto, Member currentMember) {
        Optional<Member> current = memberRepository.findById(currentMember.getId());
        Optional<Member> friend = memberRepository.findById(alarmFriendRequestDto.getToFriend().getId());
        alarmFriendRequestDto.insertMember(friend.get());
        alarmRepository.save(AlarmFriendRequestDto.friendToEntity(alarmFriendRequestDto, current.get().getNickname()));

        return ResponseEntity.ok().body("친구 요청 알람 보내기 완료");
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
