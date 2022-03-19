package com.project.moabuja.service;

import com.project.moabuja.domain.friend.Friend;
import com.project.moabuja.domain.member.Member;
import com.project.moabuja.dto.request.friend.FriendInvitationDelete;
import com.project.moabuja.dto.request.friend.FriendInvitationRequestDto;

public interface FriendService {

    public Friend save(FriendInvitationRequestDto friendRequestDto, Member currentUser);

    public void deleteFriend(FriendInvitationDelete friendDelete, Member currentUser);
}
