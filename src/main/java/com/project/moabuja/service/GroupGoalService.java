package com.project.moabuja.service;

import com.project.moabuja.domain.goal.ChallengeGoal;
import com.project.moabuja.domain.goal.GroupGoal;
import com.project.moabuja.domain.member.Member;
import com.project.moabuja.dto.request.goal.CreateChallengeRequestDto;
import com.project.moabuja.dto.request.goal.CreateGroupRequestDto;
import com.project.moabuja.dto.response.goal.ChallengeResponseDto;
import com.project.moabuja.dto.response.goal.CreateChallengeResponseDto;
import com.project.moabuja.dto.response.goal.CreateGroupResponseDto;
import com.project.moabuja.dto.response.goal.GroupResponseDto;
import org.springframework.http.ResponseEntity;

public interface GroupGoalService {

    public ResponseEntity<String> save(CreateGroupRequestDto groupRequestDto, Member currentUser);

    public ResponseEntity<GroupResponseDto> getGroupInfo(Member currentUser);

    public ResponseEntity<CreateGroupResponseDto> getGroupMemberCandidates(Member currentUser);

    public ResponseEntity<String> exitChallenge(Long id);

}
