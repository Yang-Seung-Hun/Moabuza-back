package com.project.moabuja.repository;

import com.project.moabuja.domain.alarm.Alarm;
import com.project.moabuja.domain.alarm.AlarmDetailType;
import com.project.moabuja.domain.alarm.AlarmType;
import com.project.moabuja.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm,Long> {

    List<Alarm> findAllByMember(Member member);

    @Query ("select a from Alarm a where a.member = :member and a.alarmType = :alarmType order by a.createdAt desc")
    List<Alarm> findAlarmsByMemberAndAlarmTypeOrderByCreatedAtDesc(@Param("member") Member member, @Param("alarmType") AlarmType alarmType);

    List<Alarm> findAlarmsByWaitingGoalIdAndAlarmDetailType(Long id, AlarmDetailType alarmDetailType);

    List<Alarm> findAlarmsByFriendNicknameAndAlarmDetailType(String nickname, AlarmDetailType invite);
}
