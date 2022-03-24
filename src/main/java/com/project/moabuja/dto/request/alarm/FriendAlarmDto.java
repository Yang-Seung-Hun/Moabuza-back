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

    public void insertMember(Member member) {
        this.member = member;
    }

    @Builder
    public FriendAlarmDto(AlarmType alarmType, String friendNickname, Member member) {
        this.alarmType = AlarmType.FRIEND;
        this.alarmDetailType = AlarmDetailType.request;
        this.friendNickname = friendNickname;
        this.member = member;
    }

    public static Alarm friendToEntity(FriendAlarmDto friendAlarmDto, String friendNickname) {
        return Alarm.builder()
                .alarmType(AlarmType.FRIEND)
                .alarmDetailType(AlarmDetailType.request)
                .friendNickname(friendNickname)
                .member(friendAlarmDto.getMember())
                .build();
    }
}
