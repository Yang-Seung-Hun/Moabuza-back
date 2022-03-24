package com.project.moabuja.service;

import com.project.moabuja.domain.friend.Friend;
import com.project.moabuja.domain.member.Member;
import com.project.moabuja.dto.request.friend.FriendInvitationDelete;
import com.project.moabuja.dto.request.friend.FriendInvitationRequestDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface FriendService {

    ResponseEntity<List<Friend>> listFriend(Member currentMember);

    void save(Member currentMember, String friendNickname);

    ResponseEntity<String> deleteFriend(Member currentMember, String friendNickname);
}
