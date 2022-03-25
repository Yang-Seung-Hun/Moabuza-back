package com.project.moabuja.service;

import com.project.moabuja.domain.alarm.Alarm;
import com.project.moabuja.domain.alarm.AlarmType;
import com.project.moabuja.domain.friend.Friend;
import com.project.moabuja.domain.member.Member;
import com.project.moabuja.dto.response.friend.FriendListDto;
import com.project.moabuja.dto.response.friend.FriendListResponseDto;
import com.project.moabuja.dto.response.friend.FriendSearchResponseDto;
import com.project.moabuja.repository.AlarmRepository;
import com.project.moabuja.repository.FriendRepository;
import com.project.moabuja.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService{

    private final FriendRepository friendRepository;
    private final MemberRepository memberRepository;
    private final AlarmRepository alarmRepository;

    @Override
    public ResponseEntity<FriendListResponseDto> listFriend(Member currentMember) {
        List<Friend> friendList = friendRepository.findFriendsByMember(currentMember);
        List<FriendListDto> friendListDto = friendList.stream().map(friend -> {
            return new FriendListDto(friend.getFriend().getNickname(), friend.getFriend().getHero());
        }).collect(Collectors.toList());

        List<Alarm> waitingFriendList = alarmRepository.findAlarmsByFriendNicknameAndAlarmType(currentMember.getNickname(), AlarmType.FRIEND);
        List<FriendListDto> waitingFriendListDto = waitingFriendList.stream().map(friend -> {
            return new FriendListDto(friend.getMember().getNickname(), friend.getMember().getHero());
        }).collect(Collectors.toList());

        FriendListResponseDto friendListResponseDto = new FriendListResponseDto(waitingFriendListDto, friendListDto);

        return ResponseEntity.ok().body(friendListResponseDto);
    }

    @Transactional
    @Override
    public void save(Member currentMember, String friendNickname) {
        Member friend = memberRepository.findMemberByNickname(friendNickname).get();

        friendRepository.save(new Friend(currentMember, friend));
        friendRepository.save(new Friend(friend, currentMember));
    }

    @Override
    public ResponseEntity<FriendSearchResponseDto> searchFriend(String friendNickname) {
        Optional<Member> friend = memberRepository.findMemberByNickname(friendNickname);
        if (friend.isPresent()) {
            FriendSearchResponseDto friendSearchResponseDto = new FriendSearchResponseDto(true, friend.get().getNickname());
            return ResponseEntity.ok().body(friendSearchResponseDto);
        } else {
            FriendSearchResponseDto friendSearchResponseDto = new FriendSearchResponseDto(false, null);
            return ResponseEntity.ok().body(friendSearchResponseDto);
        }
    }

    @Transactional
    @Override
    public ResponseEntity<String> deleteFriend(Member currentMember, String friendNickname) {
        Member friend = memberRepository.findMemberByNickname(friendNickname).get();

        friendRepository.delete(friendRepository.findByMemberAndFriend(currentMember, friend));
        friendRepository.delete(friendRepository.findByMemberAndFriend(friend, currentMember));

        return ResponseEntity.ok().body("친구 삭제 완료");
    }
}
