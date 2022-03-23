package com.project.moabuja.domain.goal;

import com.project.moabuja.domain.member.Member;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class DoneGoal {

    @Id
    @GeneratedValue
    @Column(name = "done_goal_id")
    private Long id;

    private String doneGoalName;

    private int doneGoalAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private GoalType goalType;

    public void changeMember(Member member) {
        this.member = member;
    }

    @Builder
    public DoneGoal(String doneGoalName, int doneGoalAmount, Member member, GoalType goalType) {
        this.doneGoalName = doneGoalName;
        this.doneGoalAmount = doneGoalAmount;
        this.member = member;
        this.goalType = goalType;
    }

    protected DoneGoal () {}
}
