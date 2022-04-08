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
public class FriendAlarmDto {

    private AlarmType alarmType;
    private AlarmDetailType alarmDetailType;
    private String friendNickname;
    private Member member;

    @Builder
    public FriendAlarmDto(AlarmDetailType alarmDetailType, String friendNickname, Member member) {
        this.alarmType = AlarmType.FRIEND;
        this.alarmDetailType = alarmDetailType;
        this.friendNickname = friendNickname;
        this.member = member;
    }

    public static Alarm friendToEntity(AlarmDetailType alarmDetailType, Member member, String friendNickname) {
        return Alarm.builder()
                .alarmType(AlarmType.FRIEND)
                .alarmDetailType(alarmDetailType)
                .friendNickname(friendNickname)
                .member(member)
                .build();
    }
}
