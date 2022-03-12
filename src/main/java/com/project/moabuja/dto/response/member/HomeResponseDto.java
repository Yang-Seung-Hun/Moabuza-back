package com.project.moabuja.dto.response.member;

import com.project.moabuja.domain.member.Hero;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class HomeResponseDto {
    private int groupCurrentAmount;
    private int groupNeedAmount;
    private int groupAmount;
    private int groupPercent;
    private String groupName;

    private int challengeCurrentAmount;
    private int challengeNeedAmount;
    private int challengeAmount;
    private int challengePercent;
    private String challengeName;

    private Hero hero;
    private int heroLevel;

    private int totalAmount;
    private int wallet;

}