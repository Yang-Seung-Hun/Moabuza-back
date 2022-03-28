package com.project.moabuja.service;

import com.project.moabuja.domain.goal.ChallengeGoal;
import com.project.moabuja.domain.goal.GroupGoal;
import com.project.moabuja.domain.member.Member;
import com.project.moabuja.dto.request.alarm.GoalAlarmRequestDto;
import com.project.moabuja.dto.request.goal.CreateChallengeRequestDto;
import com.project.moabuja.dto.request.goal.CreateGroupRequestDto;
import com.project.moabuja.dto.response.goal.ChallengeResponseDto;
import com.project.moabuja.dto.response.goal.CreateChallengeResponseDto;
import com.project.moabuja.dto.response.goal.CreateGroupResponseDto;
import com.project.moabuja.dto.response.goal.GroupResponseDto;
import org.springframework.http.ResponseEntity;

public interface GroupGoalService {

    ResponseEntity<String> save(CreateGroupRequestDto groupRequestDto, Member currentUser);

    ResponseEntity<GroupResponseDto> getGroupInfo(Member currentUser);

    ResponseEntity<CreateGroupResponseDto> getGroupMemberCandidates(Member currentUser);

    ResponseEntity<String> postGroup(Member currentMember, GoalAlarmRequestDto goalAlarmRequestDto);

    ResponseEntity<String> postGroupAccept(Member currentMember, Long alarmId);

    ResponseEntity<String> postGroupRefuse(Member currentMember, Long alarmId);

    ResponseEntity<String> exitGroup(Member currentMember, Long id);

    ResponseEntity<String> exitWaitingGroup(Member currentMember, Long id);


}
