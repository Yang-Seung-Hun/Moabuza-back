package com.project.moabuja.repository;

import com.project.moabuja.domain.goal.MemberWaitingGoal;
import com.project.moabuja.domain.goal.WaitingGoal;
import com.project.moabuja.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberWaitingGoalRepository extends JpaRepository<MemberWaitingGoal, Long> {

    MemberWaitingGoal findMemberWaitingGoalByMemberAndWaitingGoal(Member member, WaitingGoal waitingGoal);

    List<MemberWaitingGoal> findMemberWaitingGoalsByWaitingGoal(WaitingGoal waitingGoal);
    List<MemberWaitingGoal> findMemberWaitingGoalsByMember(Member member);
}
