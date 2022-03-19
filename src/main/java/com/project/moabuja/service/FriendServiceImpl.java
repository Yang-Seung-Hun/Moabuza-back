package com.project.moabuja.service;

import com.project.moabuja.domain.friend.Friend;
import com.project.moabuja.domain.member.Member;
import com.project.moabuja.dto.request.friend.FriendInvitationDelete;
import com.project.moabuja.dto.request.friend.FriendInvitationRequestDto;
import com.project.moabuja.repository.FriendRepository;
import com.project.moabuja.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService{

    private final FriendRepository friendRepository;
    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public void save(FriendInvitationRequestDto friendRequestDto, Member current) {

        Optional<Member> currentUserTemp = memberRepository.findById(current.getId());
        Member currentUser = currentUserTemp.get();
        Member friend = memberRepository.findMemberByNickname(friendRequestDto.getNickname()).get();

        Friend saveFriend = new Friend();
        saveFriend.changeUser(currentUser, friend);
        friendRepository.save(saveFriend);
    }

    @Transactional
    @Override
    public void deleteFriend(FriendInvitationDelete friendDelete, Member current) {
        Optional<Member> currentUserTemp = memberRepository.findById(current.getId());
        Member currentUser = currentUserTemp.get();
        Member friend = memberRepository.findMemberByNickname(friendDelete.getNickname()).get();

        Friend deleteFriend = friendRepository.findByMemberAndFriend(currentUser, friend);
        friendRepository.delete(deleteFriend);
    }
}
