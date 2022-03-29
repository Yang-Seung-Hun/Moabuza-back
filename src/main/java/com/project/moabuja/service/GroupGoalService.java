package com.project.moabuja.service;

import com.project.moabuja.domain.member.Member;
import com.project.moabuja.dto.request.alarm.GoalAlarmRequestDto;
import com.project.moabuja.dto.request.goal.CreateGroupRequestDto;
import com.project.moabuja.dto.response.goal.CreateGroupResponseDto;
import com.project.moabuja.dto.response.goal.GroupResponseDto;
import com.project.moabuja.model.GroupAcceptResponse;
import com.project.moabuja.model.GroupExitResponse;
import com.project.moabuja.model.GroupPostResponse;
import com.project.moabuja.model.GroupRefuseResponse;
import org.springframework.http.ResponseEntity;

public interface GroupGoalService {

    void save(CreateGroupRequestDto groupRequestDto, Member currentUser);

    ResponseEntity<GroupResponseDto> getGroupInfo(Member currentUser);

    ResponseEntity<CreateGroupResponseDto> getGroupMemberCandidates(Member currentUser);

    ResponseEntity<GroupPostResponse> postGroup(Member currentMember, GoalAlarmRequestDto goalAlarmRequestDto);

    ResponseEntity<GroupAcceptResponse> postGroupAccept(Member currentMember, Long alarmId);

    ResponseEntity<GroupRefuseResponse> postGroupRefuse(Member currentMember, Long alarmId);

    ResponseEntity<GroupExitResponse> exitGroup(Member currentMember, Long id);

    ResponseEntity<GroupExitResponse> exitWaitingGroup(Long id);


}
