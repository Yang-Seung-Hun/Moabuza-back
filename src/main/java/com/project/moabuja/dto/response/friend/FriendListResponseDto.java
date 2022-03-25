package com.project.moabuja.dto.response.friend;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class FriendListResponseDto {

    private List<FriendListDto> waitingFriendListDto;
    private List<FriendListDto> friendListDto;

    public FriendListResponseDto(List<FriendListDto> waitingFriendListDto, List<FriendListDto> friendListDto) {
        this.waitingFriendListDto = waitingFriendListDto;
        this.friendListDto = friendListDto;
    }
}
