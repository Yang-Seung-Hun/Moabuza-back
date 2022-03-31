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

    @Query ("select a from Alarm a where a.friendNickname = :friendNickname and a.alarmType = :alarmType and a.alarmDetailType = :alarmDetailType")
    List<Alarm> findAlarmsByFriendNicknameAndAlarmTypeAndAlarmDetailType(@Param("friendNickname") String friendNickname,
                                                                         @Param("alarmType") AlarmType alarmType, @Param("alarmDetailType") AlarmDetailType alarmDetailType);

    @Query ("select a from Alarm a where a.member = :member and a.alarmType = :alarmType and a.alarmDetailType = :alarmDetailType")
    List<Alarm> findAlarmsByMemberAndAlarmTypeAndAlarmDetailType(@Param("member") Member member,
                                                                 @Param("alarmType") AlarmType alarmType, @Param("alarmDetailType") AlarmDetailType alarmDetailType);

    @Query ("select a from Alarm a where a.member = :member and a.friendNickname = :friendNickname and a.alarmType = :alarmType and a.alarmDetailType = :alarmDetailType")
    Alarm findAlarmByMemberAndFriendNicknameAndAlarmTypeAndAlarmDetailType(@Param("member") Member member, @Param("friendNickname") String friendNickname,
                                                                           @Param("alarmType") AlarmType alarmType, @Param("alarmDetailType") AlarmDetailType alarmDetailType);
    Alarm findByMemberAndFriendNickname(Member member, String friendNickname);
    Alarm findAlarmById(Long Id);
}
