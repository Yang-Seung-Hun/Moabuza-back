package com.project.moabuja.dto.request.goal;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class CreateChallengeRequestDto {

    private String createChallengeName;
    @Positive
    private int createChallengeAmount;
    private List<String> challengeFriends = new ArrayList<>();

    @Builder
    public CreateChallengeRequestDto(String createChallengeName, int createChallengeAmount, List<String> challengeFriends) {
        this.createChallengeName = createChallengeName;
        this.createChallengeAmount = createChallengeAmount;
        this.challengeFriends = challengeFriends;
    }
}
