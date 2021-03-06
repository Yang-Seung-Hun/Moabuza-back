package com.project.moabuja.dto.response.goal;

import com.project.moabuja.domain.member.Hero;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GroupMemberDto {
    private String groupMemberNickname;
    private Hero groupMemberHero;

    public GroupMemberDto(String groupMemberNickname, Hero groupMemberHero) {
        this.groupMemberNickname = groupMemberNickname;
        this.groupMemberHero = groupMemberHero;
    }
}
