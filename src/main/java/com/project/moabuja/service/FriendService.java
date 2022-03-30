package com.project.moabuja.service;

import com.project.moabuja.domain.member.Member;
import com.project.moabuja.dto.Msg;
import com.project.moabuja.dto.request.alarm.FriendAlarmDto;
import com.project.moabuja.dto.request.friend.FriendRequestDto;
import com.project.moabuja.dto.response.friend.FriendListResponseDto;
import com.project.moabuja.dto.response.friend.FriendSearchResponseDto;
import org.springframework.http.ResponseEntity;

public interface FriendService {

    ResponseEntity<FriendListResponseDto> listFriend(Member currentMember);

    void save(Member currentMember, Member friend);

    ResponseEntity<FriendSearchResponseDto> searchFriend(FriendRequestDto friendRequestDto, Member currentMember);

    ResponseEntity<Msg> postFriend(FriendAlarmDto friendAlarmDto, Member currentMember);

    ResponseEntity<Msg> postFriendAccept(Member currentMember, Long alarmId);

    ResponseEntity<Msg> postFriendRefuse(Member currentMember, Long alarmId);

    ResponseEntity<Msg> deleteFriend(Member currentMember, FriendRequestDto friendRequestDto);
}
