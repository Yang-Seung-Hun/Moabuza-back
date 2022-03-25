package com.project.moabuja.dto.response.friend;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FriendSearchResponseDto {

    private boolean nicknameValid;
    private String nickname;

    public FriendSearchResponseDto(boolean nicknameValid, String nickname) {
        this.nicknameValid = nicknameValid;
        this.nickname =nickname;
    }
}
