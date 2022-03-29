package com.project.moabuja.service;

import com.project.moabuja.domain.member.Member;
import com.project.moabuja.dto.request.alarm.FriendAlarmDto;
import com.project.moabuja.dto.request.friend.FriendRequestDto;
import com.project.moabuja.dto.response.friend.FriendListResponseDto;
import com.project.moabuja.dto.response.friend.FriendSearchResponseDto;
import org.springframework.http.ResponseEntity;

public interface FriendService {

    ResponseEntity<FriendListResponseDto> listFriend(Member currentMember);

    void save(Member currentMember, Member friend);

    ResponseEntity<FriendSearchResponseDto> searchFriend(FriendRequestDto friendRequestDto);

    ResponseEntity<String> postFriend(FriendAlarmDto friendAlarmDto, Member currentMember);

    ResponseEntity<String> postFriendAccept(Member currentMember, Long alarmId);

    ResponseEntity<String> postFriendRefuse(Member currentMember, Long alarmId);

    ResponseEntity<String> deleteFriend(Member currentMember, FriendRequestDto friendRequestDto);
}
