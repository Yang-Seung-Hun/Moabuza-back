package com.project.moabuja.dto.request.member;

import com.project.moabuja.domain.member.Hero;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Builder
@Setter
public class MemberUpdateRequestDto {

    private String nickname;
    @Enumerated(EnumType.STRING)
    private Hero hero;
}
