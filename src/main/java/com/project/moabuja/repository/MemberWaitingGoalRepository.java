package com.project.moabuja.repository;

import com.project.moabuja.domain.goal.MemberWaitingGoal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberWaitingGoalRepository extends JpaRepository<MemberWaitingGoal, Long> {
}
