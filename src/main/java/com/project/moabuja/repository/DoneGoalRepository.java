package com.project.moabuja.repository;

import com.project.moabuja.domain.goal.DoneGoal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoneGoalRepository extends JpaRepository<DoneGoal,Long> {
}
