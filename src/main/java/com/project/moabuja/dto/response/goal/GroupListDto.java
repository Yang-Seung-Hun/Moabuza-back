package com.project.moabuja.dto.response.goal;

import com.project.moabuja.domain.member.Hero;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class GroupListDto {
    private LocalDateTime groupDate;
    private Hero hero;
    private String nickname;
    private String groupMemo;
    private int groupAmount;

    public GroupListDto(LocalDateTime groupDate, Hero hero, String nickname, String groupMemo, int groupAmount) {
        this.groupDate = groupDate;
        this.hero = hero;
        this.nickname = nickname;
        this.groupMemo = groupMemo;
        this.groupAmount = groupAmount;
    }
}
