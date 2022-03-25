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

    public static Alarm goalToEntity(GoalAlarmRequestDto requestDto, Long waitingGoalId, AlarmType alarmType, AlarmDetailType alarmDetailType, String friendNickname, Member member) {
        return Alarm.builder()
                .alarmType(alarmType)
                .alarmDetailType(alarmDetailType)
                .goalName(requestDto.getGoalName())
                .goalAmount(requestDto.getGoalAmount())
                .waitingGoalId(waitingGoalId)
                .friendNickname(friendNickname)
                .member(member)
                .build();
    }
}
