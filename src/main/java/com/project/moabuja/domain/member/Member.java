package com.project.moabuja.domain.member;

import com.project.moabuja.domain.alarm.Alarm;
import com.project.moabuja.domain.goal.ChallengeGoal;
import com.project.moabuja.domain.goal.DoneGoal;
import com.project.moabuja.domain.goal.GroupGoal;
import com.project.moabuja.domain.hero.Hero;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String email;

    private String nickname;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hero_id")
    private Hero hero;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "challenge_goal_id")
    private ChallengeGoal challengeGoal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_goal_id")
    private GroupGoal groupGoal;

    @OneToMany(mappedBy = "member")
    private List<DoneGoal> doneGaols = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Alarm> alarms = new ArrayList<>();

    //테스트용 생성자임
    public Member(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;
    }

    public void changeChallengeGoal(ChallengeGoal challengeGoal){
        this.challengeGoal = challengeGoal;
    }

    public void addAlarm(Alarm alarm){
        this.alarms.add(alarm);
        alarm.changeMember(this);
    }

    public void addDoneGoal(DoneGoal doneGoal){
        this.doneGaols.add(doneGoal);
        doneGoal.changeMember(this);
    }

}
