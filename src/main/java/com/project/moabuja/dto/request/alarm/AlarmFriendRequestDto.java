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
public class AlarmFriendRequestDto {

    private AlarmType alarmType;
    private String alarmContents;
    private String fromNickname;
    private Member toFriend;

    public void insertMember(Member toFriend) {
        this.toFriend = toFriend;
    }

    @Builder
    public AlarmFriendRequestDto(AlarmType alarmType, String alarmContents, LocalDateTime alarmTime, String fromNickname, Member toFriend) {
        this.alarmType = alarmType;
        this.alarmContents = alarmContents;
        this.fromNickname = fromNickname;
        this.toFriend = toFriend;
    }

    public static Alarm friendToEntity(AlarmFriendRequestDto requestDto, String fromNickname) {
        String emptycontent = "내용없음";
        return Alarm.builder()
                .alarmType(requestDto.getAlarmType())
                .alarmContents(emptycontent)
                .fromNickname(fromNickname)
                .member(requestDto.getToFriend())
                .build();
    }
}
