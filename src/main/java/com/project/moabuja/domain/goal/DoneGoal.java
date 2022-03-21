package com.project.moabuja.domain.goal;

import com.project.moabuja.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
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
    private DoneGoalType doneGoalType;

    public void changeMember(Member member) {
        this.member = member;
    }

    @Builder
    public DoneGoal(String doneGoalName, int doneGoalAmount, Member member, DoneGoalType doneGoalType) {
        this.doneGoalName = doneGoalName;
        this.doneGoalAmount = doneGoalAmount;
        this.member = member;
        this.doneGoalType = doneGoalType;
    }
}
