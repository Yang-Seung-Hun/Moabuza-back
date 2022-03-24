package com.project.moabuja.dto.response.goal;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class GroupResponseDto {
    private String goalStatus;
    private List<GroupMemberDto> groupMembers = new ArrayList<>();
    private String groupName;
    private int groupLeftAmount;
    private int groupNowPercent;
    private List<String> groupDoneGoals = new ArrayList<>();
    private List<GroupListDto> groupLists = new ArrayList<>();
    private List<WaitingGoalResponseDto> waitingGoals = new ArrayList<>();

    @Builder
    public GroupResponseDto(String goalStatus, List<GroupMemberDto> groupMembers, String groupName, int groupLeftAmount, int groupNowPercent, List<String> groupDoneGoals, List<GroupListDto> groupLists, List<WaitingGoalResponseDto> waitingGoals) {
        this.goalStatus = goalStatus;
        this.groupMembers = groupMembers;
        this.groupName = groupName;
        this.groupLeftAmount = groupLeftAmount;
        this.groupNowPercent = groupNowPercent;
        this.groupDoneGoals = groupDoneGoals;
        this.groupLists = groupLists;
        this.waitingGoals = waitingGoals;
    }
}
