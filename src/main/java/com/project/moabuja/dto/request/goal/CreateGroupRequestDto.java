package com.project.moabuja.dto.request.goal;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class CreateGroupRequestDto {
    private String createGroupName;
    private int createGroupAmount;
    private List<String> groupFriends = new ArrayList<>();

    @Builder
    public CreateGroupRequestDto(String createGroupName, int createGroupAmount, List<String> groupFiends) {
        this.createGroupName = createGroupName;
        this.createGroupAmount = createGroupAmount;
        this.groupFriends = groupFiends;
    }
}
