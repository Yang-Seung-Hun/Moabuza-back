package com.project.moabuja.domain.goal;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Entity
public class GroupGoal {

    @Id
    @GeneratedValue
    @Column(name = "group_goal_id")
    private Long id;

    private String groupGoalName;

    private int groupGoalAmount;

    private int groupCurrentAmount;

    private boolean isAcceptedGroup;
}
