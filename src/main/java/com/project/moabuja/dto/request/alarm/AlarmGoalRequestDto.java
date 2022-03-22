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
    private String fromNickname;
    private Member toFriend;

    public void insertMember(Member toFriend) {
        this.toFriend = toFriend;
    }

    @Builder
    public AlarmGoalRequestDto(AlarmType alarmType, String alarmContents, String fromNickname, Member toFriend) {
        this.alarmType = alarmType;
        this.alarmContents =alarmContents;
        this.fromNickname = fromNickname;
        this.toFriend = toFriend;
    }

    public static Alarm goalToEntity(AlarmGoalRequestDto requestDto, String fromNickname) {
        return Alarm.builder()
                .alarmType(requestDto.getAlarmType())
                .alarmContents(requestDto.getAlarmContents())
                .fromNickname(fromNickname)
                .member(requestDto.getToFriend())
                .build();
    }
}
