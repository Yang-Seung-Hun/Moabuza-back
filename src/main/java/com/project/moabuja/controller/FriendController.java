package com.project.moabuja.controller;

import com.project.moabuja.domain.member.Member;
import com.project.moabuja.dto.request.friend.FriendInvitationDelete;
import com.project.moabuja.dto.request.friend.FriendInvitationRequestDto;
import com.project.moabuja.dto.request.goal.CreateChallengeRequestDto;
import com.project.moabuja.security.userdetails.UserDetailsImpl;
import com.project.moabuja.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;

    @PostMapping("/friends")
    public ResponseEntity PostCreateFriend(@RequestBody FriendInvitationRequestDto friendInvitationRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Member currentUser = userDetails.getMember();
        friendService.save(friendInvitationRequestDto, currentUser);
        return ResponseEntity.ok().body("친구 추가 완료");
    }

    @DeleteMapping("/friends")
    public ResponseEntity DeleteFriend(@RequestBody FriendInvitationDelete friendInvitationDelete, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Member currentUser = userDetails.getMember();
        friendService.deleteFriend(friendInvitationDelete, currentUser);
        return ResponseEntity.ok().body("친구 삭제 완료");
    }

}
