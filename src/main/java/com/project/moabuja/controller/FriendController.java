package com.project.moabuja.controller;

import com.project.moabuja.domain.member.Member;
import com.project.moabuja.dto.request.friend.FriendInvitationDelete;
import com.project.moabuja.dto.request.friend.FriendInvitationRequestDto;
import com.project.moabuja.dto.request.goal.CreateChallengeRequestDto;
import com.project.moabuja.security.userdetails.UserDetailsImpl;
import com.project.moabuja.service.FriendService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;

    @ApiOperation(value = "친구 수락")
    @PostMapping("/friends")
    public ResponseEntity<String> postCreateFriend(@RequestBody FriendInvitationRequestDto friendInvitationRequestDto,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Member currentUser = userDetails.getMember();
        return friendService.save(friendInvitationRequestDto, currentUser);
    }

    @ApiOperation(value = "친구 삭제")
    @DeleteMapping("/friends")
    public ResponseEntity<String> deleteFriend(@RequestBody FriendInvitationDelete friendInvitationDelete,
                                       @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Member currentUser = userDetails.getMember();
        return friendService.deleteFriend(friendInvitationDelete, currentUser);
    }

}
