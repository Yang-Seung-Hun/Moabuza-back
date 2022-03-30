package com.project.moabuja.repository;

import com.project.moabuja.domain.goal.GoalType;
import com.project.moabuja.domain.goal.MemberWaitingGoal;
import com.project.moabuja.domain.goal.WaitingGoal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WaitingGoalRepository extends JpaRepository<WaitingGoal,Long> {

    WaitingGoal findWaitingGoalByMemberWaitingGoalAndGoalType(MemberWaitingGoal memberWaitingGoal, GoalType goalType);
    WaitingGoal findWaitingGoalById(Long waitingGoalId);
}
