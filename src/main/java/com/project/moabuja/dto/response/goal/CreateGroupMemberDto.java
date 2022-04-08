package com.project.moabuja.dto.response.goal;

import com.project.moabuja.domain.member.Hero;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class CreateGroupMemberDto {
    private String groupMemberNickname;
    private boolean groupMemberCanInvite;
    private Hero hero;

    public CreateGroupMemberDto(String groupMemberNickname, boolean groupMemberCanInvite, Hero hero) {
        this.groupMemberNickname = groupMemberNickname;
        this.groupMemberCanInvite = groupMemberCanInvite;
        this.hero = hero;
    }
}
