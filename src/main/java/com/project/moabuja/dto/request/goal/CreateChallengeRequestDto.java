package com.project.moabuja.dto.request.goal;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class CreateChallengeRequestDto {

    private String createChallengeName;
    private int createChallengeAmount;
    private List<String> challengeFiends = new ArrayList<>();

    public CreateChallengeRequestDto(String createChallengeName, int createChallengeAmount, List<String> challengeFiends) {
        this.createChallengeName = createChallengeName;
        this.createChallengeAmount = createChallengeAmount;
        this.challengeFiends = challengeFiends;
    }
}
