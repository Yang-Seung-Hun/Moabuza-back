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

    private boolean isAcceptedGoal;

    @Enumerated(EnumType.STRING)
    private GoalType goalType;

    @OneToMany(mappedBy = "waitingGoal", cascade = CascadeType.ALL)
    private List<MemberWaitingGoal> memberWaitingGoals = new ArrayList<>();

    @Builder
    public WaitingGoal(String waitingGoalName, int waitingGoalAmount, GoalType goalType, boolean isAcceptedGoal) {
        this.waitingGoalName = waitingGoalName;
        this.waitingGoalAmount = waitingGoalAmount;
        this.goalType = goalType;
        this.isAcceptedGoal = isAcceptedGoal;
    }

    protected WaitingGoal () {}

}
