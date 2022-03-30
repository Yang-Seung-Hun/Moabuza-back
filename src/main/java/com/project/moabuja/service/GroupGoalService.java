package com.project.moabuja.service;

import com.project.moabuja.domain.member.Member;
import com.project.moabuja.dto.Res;
import com.project.moabuja.dto.request.alarm.GoalAlarmRequestDto;
import com.project.moabuja.dto.request.goal.CreateGroupRequestDto;
import com.project.moabuja.dto.response.goal.CreateGroupResponseDto;
import com.project.moabuja.dto.response.goal.GroupResponseDto;
import org.springframework.http.ResponseEntity;

public interface GroupGoalService {

    ResponseEntity<Res.GroupCreateResponse> save(CreateGroupRequestDto groupRequestDto, Member currentUser);

    ResponseEntity<GroupResponseDto> getGroupInfo(Member currentUser);

    ResponseEntity<CreateGroupResponseDto> getGroupMemberCandidates(Member currentUser);

    ResponseEntity<Res.GroupPostResponse> postGroup(Member currentMember, GoalAlarmRequestDto goalAlarmRequestDto);

    ResponseEntity<Res.GroupAcceptResponse> postGroupAccept(Member currentMember, Long alarmId);

    ResponseEntity<Res.GroupRefuseResponse> postGroupRefuse(Member currentMember, Long alarmId);

    ResponseEntity<Res.GroupExitResponse> exitGroup(Member currentMember, Long id);

    ResponseEntity<Res.GroupExitResponse> exitWaitingGroup(Long id);


}
