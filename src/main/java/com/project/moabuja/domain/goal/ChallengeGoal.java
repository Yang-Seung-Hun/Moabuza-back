package com.project.moabuja.domain.goal;

import com.project.moabuja.domain.member.Member;
import com.project.moabuja.dto.request.goal.CreateChallengeRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class ChallengeGoal {

    @Id @GeneratedValue
    @Column(name = "challenge_goal_id")
    private Long id;

    private String challengeGoalName;

    private int challengeGoalAmount;

    private int currentAmount;

    private boolean isAcceptedChallenge;

    @OneToMany(mappedBy = "challengeGoal",cascade = CascadeType.ALL)
    private List<Member> members = new ArrayList<>();

    public ChallengeGoal(String challengeGoalName, int challengeGoalAmount, int currentAmount, boolean isAcceptedChallenge) {
        this.challengeGoalName = challengeGoalName;
        this.challengeGoalAmount = challengeGoalAmount;
        this.currentAmount = currentAmount;
        this.isAcceptedChallenge = isAcceptedChallenge;
    }

    public void addMember(Member member){
        this.members.add(member);
        member.changeChallengeGoal(this);
    }

    public void removeMember(Member member){
        this.members.remove(member);
        member.changeChallengeGoal(null);
    }

    //테스트용 setter입니다.
    public void setIsAcceptedChallenge(Boolean bool){
        this.isAcceptedChallenge = bool;
    }
}
