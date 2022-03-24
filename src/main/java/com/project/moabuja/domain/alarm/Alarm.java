package com.project.moabuja.domain.alarm;

import com.project.moabuja.domain.Timestamped;
import com.project.moabuja.domain.member.Member;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
public class Alarm extends Timestamped {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private AlarmType alarmType;

    @Enumerated(EnumType.STRING)
    private AlarmDetailType alarmDetailType;

    private String alarmContents;

    private String friendNickname;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Alarm(AlarmType alarmType, AlarmDetailType alarmDetailType, String alarmContents, String friendNickname, Member member) {
        this.alarmType = alarmType;
        this.alarmDetailType = alarmDetailType;
        this.alarmContents = alarmContents;
        this.friendNickname = friendNickname;
        this.member = member;
    }

    public void changeMember(Member member) {
        this.member = member;
    }

    protected Alarm () {}
}
