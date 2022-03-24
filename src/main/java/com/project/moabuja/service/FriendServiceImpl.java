package com.project.moabuja.service;

import com.project.moabuja.domain.friend.Friend;
import com.project.moabuja.domain.member.Member;
import com.project.moabuja.dto.request.friend.FriendInvitationDelete;
import com.project.moabuja.dto.request.friend.FriendInvitationRequestDto;
import com.project.moabuja.repository.FriendRepository;
import com.project.moabuja.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService{

    private final FriendRepository friendRepository;
    private final MemberRepository memberRepository;

    @Override
    public ResponseEntity<List<Friend>> listFriend(Member currentMember) {
        List<Friend> friendList = friendRepository.findFriendsByMember(currentMember);
        return ResponseEntity.ok().body(friendList);
    }

    @Transactional
    @Override
    public void save(Member currentMember, String friendNickname) {
        Member friend = memberRepository.findMemberByNickname(friendNickname).get();

        friendRepository.save(new Friend(currentMember, friend));
        friendRepository.save(new Friend(friend, currentMember));
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
