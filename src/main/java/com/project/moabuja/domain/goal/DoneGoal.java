package com.project.moabuja.domain.goal;

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

    @Enumerated(EnumType.STRING)
    private DoneGoalType doneGoalType;
}
