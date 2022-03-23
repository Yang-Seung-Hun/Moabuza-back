package com.project.moabuja.controller;

import com.project.moabuja.domain.friend.Friend;
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

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;

    @ApiOperation(value = "친구 목록")
    @GetMapping("/friends")
    public ResponseEntity<List<Friend>> getListFriend(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Member currentUser = userDetails.getMember();
        return friendService.listFriend(currentUser);
    }

    @ApiOperation(value = "친구 수락")
    @PostMapping("/friends")
    public ResponseEntity<String> postCreateFriend(@RequestParam String friendNickname,
                                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Member currentUser = userDetails.getMember();
        return friendService.save(friendNickname, currentUser);
    }

    @ApiOperation(value = "친구 삭제")
    @DeleteMapping("/friends")
    public ResponseEntity<String> deleteFriend(@RequestParam String friendNickname,
                                               @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Member currentUser = userDetails.getMember();
        return friendService.deleteFriend(friendNickname, currentUser);
    }



}
