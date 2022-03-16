package com.project.moabuja.dto.response.goal;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class CreateGroupResponseDto {
    List<CreateGroupMemberDto> groupMembers = new ArrayList<>();

    public CreateGroupResponseDto(List<CreateGroupMemberDto> groupMembers) {
        this.groupMembers = groupMembers;
    }
}
