package com.project.moabuja.dto.request.friend;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FriendRequestDto {
    private String friendNickname;

    public FriendRequestDto(String friendNickname) {
        this.friendNickname = friendNickname;
    }
}
