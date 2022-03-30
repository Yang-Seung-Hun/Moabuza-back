package com.project.moabuja.service;

import com.project.moabuja.domain.member.Member;
import com.project.moabuja.dto.Msg;
import com.project.moabuja.dto.request.alarm.GoalAlarmRequestDto;
import com.project.moabuja.dto.request.goal.CreateGroupRequestDto;
import com.project.moabuja.dto.response.goal.CreateGroupResponseDto;
import com.project.moabuja.dto.response.goal.GroupResponseDto;
import org.springframework.http.ResponseEntity;

public interface GroupGoalService {

    ResponseEntity<Msg> save(CreateGroupRequestDto groupRequestDto, Member currentUser);

    ResponseEntity<GroupResponseDto> getGroupInfo(Member currentUser);

    ResponseEntity<CreateGroupResponseDto> getGroupMemberCandidates(Member currentUser);

    ResponseEntity<Msg> postGroup(Member currentMember, GoalAlarmRequestDto goalAlarmRequestDto);

    ResponseEntity<Msg> postGroupAccept(Member currentMember, Long alarmId);

    ResponseEntity<Msg> postGroupRefuse(Member currentMember, Long alarmId);

    ResponseEntity<Msg> exitGroup(Member currentMember, Long id);

    ResponseEntity<Msg> exitWaitingGroup(Long id);


}
