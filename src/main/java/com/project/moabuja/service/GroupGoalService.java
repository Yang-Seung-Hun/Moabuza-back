package com.project.moabuja.service;

import com.project.moabuja.domain.member.Member;
import com.project.moabuja.dto.ResponseMsg;
import com.project.moabuja.dto.request.alarm.GoalAlarmRequestDto;
import com.project.moabuja.dto.request.goal.CreateGroupRequestDto;
import com.project.moabuja.dto.response.goal.CreateGroupResponseDto;
import com.project.moabuja.dto.response.goal.GroupResponseDto;
import org.springframework.http.ResponseEntity;

public interface GroupGoalService {

    ResponseEntity<ResponseMsg> save(CreateGroupRequestDto groupRequestDto, Member currentUser);

    ResponseEntity<GroupResponseDto> getGroupInfo(Member currentUser);

    ResponseEntity<CreateGroupResponseDto> getGroupMemberCandidates(Member currentUser);

    ResponseEntity<ResponseMsg> postGroup(Member currentMember, GoalAlarmRequestDto goalAlarmRequestDto);

    ResponseEntity<ResponseMsg> postGroupAccept(Member currentMember, Long alarmId);

    ResponseEntity<ResponseMsg> postGroupRefuse(Member currentMember, Long alarmId);

    ResponseEntity<ResponseMsg> exitGroup(Member currentMember, Long id);

    ResponseEntity<ResponseMsg> exitWaitingGroup(Long id);


}
