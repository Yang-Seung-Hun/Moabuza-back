package com.project.moabuja.domain.goal;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
public class DoneGoal {

    @Id
    @GeneratedValue
    @Column(name = "done_goal_id")
    private Long id;

    private String doneGoalName;

    private int doneGoalAmount;

    private boolean doneGoalSuccess;
}
