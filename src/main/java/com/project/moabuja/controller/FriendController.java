package com.project.moabuja.controller;

import com.project.moabuja.domain.member.Member;
import com.project.moabuja.dto.Msg;
import com.project.moabuja.dto.request.alarm.FriendAlarmDto;
import com.project.moabuja.dto.request.friend.FriendRequestDto;
import com.project.moabuja.dto.response.friend.FriendListResponseDto;
import com.project.moabuja.dto.response.friend.FriendSearchResponseDto;
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

    @ApiOperation(value = "친구 목록")
    @GetMapping("/friend")
    public ResponseEntity<FriendListResponseDto> getListFriend(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Member currentUser = userDetails.getMember();
        return friendService.listFriend(currentUser);
    }

    @ApiOperation(value = "친구 검색")
    @PostMapping("/friend/search")
    public ResponseEntity<FriendSearchResponseDto> searchFriend(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                @RequestBody FriendRequestDto friendRequestDto) {
        Member currentMember = userDetails.getMember();
        return friendService.searchFriend(friendRequestDto, currentMember);
    }

    @ApiOperation(value = "친구 요청")
    @PostMapping("/friend")
    public ResponseEntity<Msg> postFriend(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                          @RequestBody FriendAlarmDto friendAlarmDto) {
        Member currentMember = userDetails.getMember();
        return friendService.postFriend(friendAlarmDto, currentMember);
    }

    @ApiOperation(value = "친구 수락")
    @PostMapping("/friend/{id}/accept")
    public ResponseEntity<Msg> postFriendAccept(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                @PathVariable Long id) {
        Member currentMember = userDetails.getMember();
        return friendService.postFriendAccept(currentMember, id);
    }

    @ApiOperation(value = "친구 거절")
    @PostMapping("/friend/{id}/refuse")
    public ResponseEntity<Msg> postFriendRefuse(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                 @PathVariable Long id) {
        Member currentMember = userDetails.getMember();
        return friendService.postFriendRefuse(currentMember, id);
    }

    @ApiOperation(value = "친구 삭제")
    @DeleteMapping("/friend")
    public ResponseEntity<Msg> deleteFriend(@RequestBody FriendRequestDto friendRequestDto,
                                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Member currentUser = userDetails.getMember();
        return friendService.deleteFriend(currentUser, friendRequestDto);
    }

}
