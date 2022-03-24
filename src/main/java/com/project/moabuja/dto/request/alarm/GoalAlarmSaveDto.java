package com.project.moabuja.dto.request.alarm;

import com.project.moabuja.domain.alarm.Alarm;
import com.project.moabuja.domain.alarm.AlarmDetailType;
import com.project.moabuja.domain.alarm.AlarmType;
import com.project.moabuja.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GoalAlarmSaveDto {

    private AlarmType alarmType;
    private AlarmDetailType alarmDetailType;
    private String goalName;
    private int goalAmount;
    private String friendNickname;
    private Member member;

    @Builder
    public GoalAlarmSaveDto(AlarmType alarmType, AlarmDetailType alarmDetailType, String goalName, int goalAmount, String friendNickname, Member member) {
        this.alarmType = alarmType;
        this.alarmDetailType = alarmDetailType;
        this.goalName = goalName;
        this.goalAmount = goalAmount;
        this.friendNickname = friendNickname;
        this.member = member;
    }

    public static Alarm goalToEntity(GoalAlarmRequestDto requestDto, AlarmType alarmType, AlarmDetailType alarmDetailType, String friendNickname, Member member) {
        return Alarm.builder()
                .alarmType(alarmType)
                .alarmDetailType(alarmDetailType)
                .goalName(requestDto.getGoalName())
                .goalAmount(requestDto.getGoalAmount())
                .friendNickname(friendNickname)
                .member(member)
                .build();
    }
}
