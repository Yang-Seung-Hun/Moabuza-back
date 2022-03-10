package com.project.moabuja.dto.request.member;

import com.project.moabuja.domain.hero.HeroName;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class memberInfoRequestDto {
    private String nickname;
    private HeroName heroName;
}
