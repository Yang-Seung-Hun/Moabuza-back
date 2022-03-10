package com.project.moabuja.domain.goal;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
public class ChallengeGoal {

    @Id @GeneratedValue
    @Column(name = "challenge_goal_id")
    private Long id;

    private String challengeGoalName;

    private int challengeGoalAmount;

    private int currentAmount;

    private boolean isAcceptedChallenge;
}
