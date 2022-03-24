package com.project.moabuja.repository;

import com.project.moabuja.domain.goal.WaitingGoal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WaitingGoalRepository extends JpaRepository<WaitingGoal,Long> {


}
