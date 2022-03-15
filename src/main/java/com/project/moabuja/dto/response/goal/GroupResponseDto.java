package com.project.moabuja.dto.response.goal;

import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@NoArgsConstructor
public class GroupResponseDto {

    private String goalStatus;
    private List<GroupMemberDto> groupMembers = new ArrayList<>();
    private String groupName;
    private int groupLeftAmount;
    private int groupNowPercent;
    private List<String> groupDoneGoals = new ArrayList<>();
}
