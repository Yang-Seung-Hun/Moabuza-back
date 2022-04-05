package com.project.moabuja.domain.goal;

import com.project.moabuja.domain.member.Member;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class WaitingGoal {

    @Id
    @GeneratedValue
    @Column(name = "waiting_goal_id")
    private Long id;

    private String waitingGoalName;

    private int waitingGoalAmount;

    @Enumerated(EnumType.STRING)
    private GoalType goalType;

    @OneToMany(mappedBy = "waitingGoal", cascade = CascadeType.ALL)
    private final List<MemberWaitingGoal> memberWaitingGoals = new ArrayList<>();

    @Builder
    public WaitingGoal(String waitingGoalName, int waitingGoalAmount, GoalType goalType) {
        this.waitingGoalName = waitingGoalName;
        this.waitingGoalAmount = waitingGoalAmount;
        this.goalType = goalType;
    }

    protected WaitingGoal () {}

    public void removeMemberWaitingGoals(MemberWaitingGoal memberWaitingGoal){
        this.memberWaitingGoals.remove(memberWaitingGoal);
        memberWaitingGoal.changeWaitingGoal(null);
    }

    public void addMemberWaitingGoals(MemberWaitingGoal memberWaitingGoal){
        this.memberWaitingGoals.add(memberWaitingGoal);
        memberWaitingGoal.changeWaitingGoal(this);
    }

}
