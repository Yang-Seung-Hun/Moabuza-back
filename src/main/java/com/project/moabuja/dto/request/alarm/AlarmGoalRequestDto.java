package com.project.moabuja.dto.request.alarm;

import com.project.moabuja.domain.alarm.Alarm;
import com.project.moabuja.domain.alarm.AlarmType;
import com.project.moabuja.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class AlarmGoalRequestDto {

    private AlarmType alarmType;
    private String alarmContents;
    private String friendNickname;
    private Member member;

    public void insertMember(Member member) {
        this.member = member;
    }

    @Builder
    public AlarmGoalRequestDto(AlarmType alarmType, String alarmContents, String friendNickname, Member member) {
        this.alarmType = alarmType;
        this.alarmContents =alarmContents;
        this.friendNickname = friendNickname;
        this.member = member;
    }

    public static Alarm goalToEntity(AlarmGoalRequestDto requestDto, String friendNickname) {
        return Alarm.builder()
                .alarmType(requestDto.getAlarmType())
                .alarmContents(requestDto.getAlarmContents())
                .friendNickname(friendNickname)
                .member(requestDto.getMember())
                .build();
    }
}
