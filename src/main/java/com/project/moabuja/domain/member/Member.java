package com.project.moabuja.domain.member;

import com.project.moabuja.domain.goal.ChallengeGoal;
import com.project.moabuja.domain.goal.DoneGoal;
import com.project.moabuja.domain.goal.GroupGoal;
import com.project.moabuja.domain.hero.Hero;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "done_goal_id")
    private DoneGoal doneGaol;

    //테스트용 생성자임
    public Member(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;
    }

    public void changeChallengeGoal(ChallengeGoal challengeGoal){
        this.challengeGoal = challengeGoal;
    }


}
