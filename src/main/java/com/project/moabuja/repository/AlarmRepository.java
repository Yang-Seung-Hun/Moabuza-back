package com.project.moabuja.repository;

import com.project.moabuja.domain.alarm.Alarm;
import com.project.moabuja.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm,Long> {

    List<Alarm> findAllByMember(Member member);
}
