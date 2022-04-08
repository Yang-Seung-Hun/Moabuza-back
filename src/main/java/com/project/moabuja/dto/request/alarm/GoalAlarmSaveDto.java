package com.project.moabuja.dto.request.alarm;

import com.project.moabuja.domain.alarm.Alarm;
import com.project.moabuja.domain.alarm.AlarmDetailType;
import com.project.moabuja.domain.alarm.AlarmType;
import com.project.moabuja.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;

@Getter
@NoArgsConstructor
public class GoalAlarmSaveDto {

    private AlarmType alarmType;
    private AlarmDetailType alarmDetailType;
    private String goalName;
    @Positive
    private int goalAmount;
    private Long waitingGoalId;
    private String friendNickname;
    private Member member;

    @Builder
    public GoalAlarmSaveDto(AlarmType alarmType, AlarmDetailType alarmDetailType, String goalName, int goalAmount, Long waitingGoalId, String friendNickname, Member member) {
        this.alarmType = alarmType;
        this.alarmDetailType = alarmDetailType;
        this.goalName = goalName;
        this.goalAmount = goalAmount;
        this.waitingGoalId = waitingGoalId;
        this.friendNickname = friendNickname;
        this.member = member;
    }

    public static Alarm goalToEntity(GoalAlarmSaveDto saveDto) {
        return Alarm.builder()
                .alarmType(saveDto.getAlarmType())
                .alarmDetailType(saveDto.getAlarmDetailType())
                .goalName(saveDto.getGoalName())
                .goalAmount(saveDto.getGoalAmount())
                .waitingGoalId(saveDto.getWaitingGoalId())
                .friendNickname(saveDto.getFriendNickname())
                .member(saveDto.getMember())
                .build();
    }
}
