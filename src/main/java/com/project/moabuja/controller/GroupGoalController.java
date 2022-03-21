package com.project.moabuja.controller;

import com.project.moabuja.domain.member.Member;
import com.project.moabuja.dto.request.goal.CreateGroupRequestDto;
import com.project.moabuja.dto.response.goal.CreateGroupResponseDto;
import com.project.moabuja.dto.response.goal.GroupResponseDto;
import com.project.moabuja.security.userdetails.UserDetailsImpl;
import com.project.moabuja.service.GroupGoalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class GroupGoalController {

    private final GroupGoalService groupGoalService;

    @GetMapping("/money/group")
    public ResponseEntity getMoneyGroup(@AuthenticationPrincipal UserDetailsImpl userDetails){

        Member currentUser = userDetails.getMember();
        GroupResponseDto groupResponseDto = groupGoalService.getGroupInfo(currentUser);

        return ResponseEntity.ok().body(groupResponseDto);
    }

    @GetMapping("/money/group/creategroup")
    public ResponseEntity getCreateGroup(@AuthenticationPrincipal UserDetailsImpl userDetails){

        Member currentUser = userDetails.getMember();
        CreateGroupResponseDto createGroupResponseDto = groupGoalService.getGroupMemberCandidates(currentUser);

        return ResponseEntity.ok().body(createGroupResponseDto);
    }

    @PostMapping("/money/group/creategroup")
    public ResponseEntity postCreateGroup(@RequestBody CreateGroupRequestDto createGroupRequestDto,
                                          @AuthenticationPrincipal UserDetailsImpl userDetails){

        Member currentUser = userDetails.getMember();
        groupGoalService.save(createGroupRequestDto,currentUser);

        return ResponseEntity.ok().body("같이해부자 생성 완료");
    }

    @GetMapping("/money/group/exitgroup/{id}")
    public ResponseEntity exitGroup(@PathVariable Long id){

        groupGoalService.exitChallenge(id);

        return ResponseEntity.ok().body("같이해부자 나가기 완료");
    }

}
