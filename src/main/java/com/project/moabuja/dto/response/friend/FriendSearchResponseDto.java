package com.project.moabuja.dto.response.friend;

import com.project.moabuja.domain.member.Hero;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FriendSearchResponseDto {

    private String nickname;
    private Hero hero;
    private String msg;

    public FriendSearchResponseDto(String nickname, Hero hero, String msg) {
        this.nickname = nickname;
        this.hero = hero;
        this.msg = msg;
    }
}
