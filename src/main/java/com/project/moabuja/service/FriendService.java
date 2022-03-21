package com.project.moabuja.service;

import com.project.moabuja.domain.friend.Friend;
import com.project.moabuja.domain.member.Member;
import com.project.moabuja.dto.request.friend.FriendInvitationDelete;
import com.project.moabuja.dto.request.friend.FriendInvitationRequestDto;
import org.springframework.http.ResponseEntity;

public interface FriendService {

    public ResponseEntity<String> save(FriendInvitationRequestDto friendRequestDto, Member currentUser);

    public ResponseEntity<String> deleteFriend(FriendInvitationDelete friendDelete, Member currentUser);
}
