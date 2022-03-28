package com.project.moabuja.dto.response.friend;

import com.project.moabuja.domain.member.Hero;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FriendSearchResponseDto {

    private boolean nicknameValid;
    private String nickname;
    private Hero hero;

    public FriendSearchResponseDto(boolean nicknameValid, String nickname, Hero hero) {
        this.nicknameValid = nicknameValid;
        this.nickname = nickname;
        this.hero = hero;
    }
}
