package com.project.moabuja.controller;

import com.project.moabuja.domain.member.Member;
import com.project.moabuja.dto.Res;
import com.project.moabuja.dto.request.alarm.FriendAlarmDto;
import com.project.moabuja.dto.request.friend.FriendRequestDto;
import com.project.moabuja.dto.response.friend.FriendListResponseDto;
import com.project.moabuja.dto.response.friend.FriendSearchResponseDto;
import com.project.moabuja.security.userdetails.UserDetailsImpl;
import com.project.moabuja.service.AlarmService;
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
    private final AlarmService alarmService;

    @ApiOperation(value = "친구 목록")
    @GetMapping("/friend")
    public ResponseEntity<FriendListResponseDto> getListFriend(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Member currentUser = userDetails.getMember();
        return friendService.listFriend(currentUser);
    }

    @ApiOperation(value = "친구 검색")
    @PostMapping("/friend/search")
    public ResponseEntity<FriendSearchResponseDto> searchFriend(@RequestBody FriendRequestDto friendRequestDto) {
        return friendService.searchFriend(friendRequestDto);
    }

    @ApiOperation(value = "친구 요청")
    @PostMapping("/friend")
    public ResponseEntity<Res.FriendPostResponse> postFriend(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                             @RequestBody FriendAlarmDto friendAlarmDto) {
        Member currentMember = userDetails.getMember();
        return friendService.postFriend(friendAlarmDto, currentMember);
    }

    @ApiOperation(value = "친구 수락")
    @PostMapping("/friend/{id}/accept")
    public ResponseEntity<Res.FriendAcceptResponse> postFriendAccept(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                 @PathVariable Long alarmId) {
        Member currentMember = userDetails.getMember();
        return friendService.postFriendAccept(currentMember, alarmId);
    }

    @ApiOperation(value = "친구 거절")
    @PostMapping("/friend/{id}/refuse")
    public ResponseEntity<Res.FriendRefuseResponse> postFriendRefuse(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                 @PathVariable Long alarmId) {
        Member currentMember = userDetails.getMember();
        return friendService.postFriendRefuse(currentMember, alarmId);
    }

    @ApiOperation(value = "친구 삭제")
    @DeleteMapping("/friend")
    public ResponseEntity<Res.FriendDeleteResponse> deleteFriend(@RequestBody FriendRequestDto friendRequestDto,
                                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Member currentUser = userDetails.getMember();
        return friendService.deleteFriend(currentUser, friendRequestDto);
    }

}
