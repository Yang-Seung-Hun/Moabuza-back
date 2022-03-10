package com.project.moabuja.dto.response.goal;

import com.project.moabuja.domain.hero.HeroName;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GroupMemberDto {

    private String groupMemberNickname;
    private HeroName groupMemberHero;
}
