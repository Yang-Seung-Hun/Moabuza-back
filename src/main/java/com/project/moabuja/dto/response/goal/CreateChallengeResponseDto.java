package com.project.moabuja.dto.response.goal;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class CreateChallengeResponseDto {
    private List<CreateChallengeMemberDto> challengeMembers = new ArrayList<>();
}
