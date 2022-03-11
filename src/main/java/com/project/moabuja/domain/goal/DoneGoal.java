package com.project.moabuja.domain.goal;

import com.project.moabuja.domain.member.Member;
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
    private DoneGoalType doneGoalType;

    public void changeMember(Member member) {
        this.member = member;
    }
}
