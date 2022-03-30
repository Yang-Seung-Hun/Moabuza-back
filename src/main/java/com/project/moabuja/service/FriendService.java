package com.project.moabuja.service;

import com.project.moabuja.domain.member.Member;
import com.project.moabuja.dto.Res;
import com.project.moabuja.dto.request.alarm.FriendAlarmDto;
import com.project.moabuja.dto.request.friend.FriendRequestDto;
import com.project.moabuja.dto.response.friend.FriendListResponseDto;
import com.project.moabuja.dto.response.friend.FriendSearchResponseDto;
import org.springframework.http.ResponseEntity;

public interface FriendService {

    ResponseEntity<FriendListResponseDto> listFriend(Member currentMember);

    void save(Member currentMember, Member friend);

    ResponseEntity<FriendSearchResponseDto> searchFriend(FriendRequestDto friendRequestDto);

    ResponseEntity<Res.FriendPostResponse> postFriend(FriendAlarmDto friendAlarmDto, Member currentMember);

    ResponseEntity<Res.FriendAcceptResponse> postFriendAccept(Member currentMember, Long alarmId);

    ResponseEntity<Res.FriendRefuseResponse> postFriendRefuse(Member currentMember, Long alarmId);

    ResponseEntity<Res.FriendDeleteResponse> deleteFriend(Member currentMember, FriendRequestDto friendRequestDto);
}
