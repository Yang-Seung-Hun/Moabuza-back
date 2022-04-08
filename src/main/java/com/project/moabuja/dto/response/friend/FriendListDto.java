package com.project.moabuja.dto.response.friend;

import com.project.moabuja.domain.member.Hero;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FriendListDto {

    private String nickname;
    private Hero hero;

    public FriendListDto(String nickname, Hero hero) {
        this.nickname = nickname;
        this.hero = hero;
    }
}
