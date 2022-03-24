package com.project.moabuja.domain.goal;

import com.project.moabuja.domain.member.Member;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class MemberWaitingGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_waiting_goal_id")
    private Long id;

    private boolean isAcceptedGoal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "waiting_goal_id")
    private WaitingGoal waitingGoal;

    public MemberWaitingGoal(Member member, WaitingGoal waitingGoal, boolean isAcceptedGoal) {
        this.member = member;
        this.waitingGoal = waitingGoal;
        this.isAcceptedGoal = isAcceptedGoal;
    }
    public void changeIsAcceptedGoal(MemberWaitingGoal memberWaitingGoal) {
        memberWaitingGoal.isAcceptedGoal = true;
    }

    protected MemberWaitingGoal () {}
}
