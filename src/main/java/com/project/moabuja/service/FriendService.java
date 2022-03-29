package com.project.moabuja.service;

import com.project.moabuja.domain.member.Member;
import com.project.moabuja.dto.request.alarm.FriendAlarmDto;
import com.project.moabuja.dto.request.friend.FriendRequestDto;
import com.project.moabuja.dto.response.friend.FriendListResponseDto;
import com.project.moabuja.dto.response.friend.FriendSearchResponseDto;
import com.project.moabuja.model.FriendAcceptResponse;
import com.project.moabuja.model.FriendDeleteResponse;
import com.project.moabuja.model.FriendPostResponse;
import com.project.moabuja.model.FriendRefuseResponse;
import org.springframework.http.ResponseEntity;

public interface FriendService {

    ResponseEntity<FriendListResponseDto> listFriend(Member currentMember);

    void save(Member currentMember, Member friend);

    ResponseEntity<FriendSearchResponseDto> searchFriend(FriendRequestDto friendRequestDto);

    ResponseEntity<FriendPostResponse> postFriend(FriendAlarmDto friendAlarmDto, Member currentMember);

    ResponseEntity<FriendAcceptResponse> postFriendAccept(Member currentMember, Long alarmId);

    ResponseEntity<FriendRefuseResponse> postFriendRefuse(Member currentMember, Long alarmId);

    ResponseEntity<FriendDeleteResponse> deleteFriend(Member currentMember, FriendRequestDto friendRequestDto);
}
