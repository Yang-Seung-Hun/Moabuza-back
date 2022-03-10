package com.project.moabuja.domain.alarm;

import com.project.moabuja.domain.member.Member;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Alarm {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private AlarmType alarmType;

    private String alarmContents;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

}
