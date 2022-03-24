package com.project.moabuja.domain.goal;

import com.project.moabuja.domain.member.Member;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class ChallengeGoal {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "challenge_goal_id")
    private Long id;

    private String challengeGoalName;

    private int challengeGoalAmount;

    private int currentAmount;

    @OneToMany(mappedBy = "challengeGoal",cascade = CascadeType.ALL)
    private List<Member> members = new ArrayList<>();

    @Builder
    public ChallengeGoal(String challengeGoalName, int challengeGoalAmount, int currentAmount) {
        this.challengeGoalName = challengeGoalName;
        this.challengeGoalAmount = challengeGoalAmount;
        this.currentAmount = currentAmount;
    }

    public void addMember(Member member){
        this.members.add(member);
        member.changeChallengeGoal(this);
    }

    public void removeMember(Member member){
        this.members.remove(member);
        member.changeChallengeGoal(null);
    }

    protected ChallengeGoal () {}
}
