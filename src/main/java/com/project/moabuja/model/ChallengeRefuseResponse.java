package com.project.moabuja.model;

import lombok.Getter;

@Getter
public class ChallengeRefuseResponse {
    private final String msg;

    public ChallengeRefuseResponse(){ this.msg = "도전해부자 요청을 거절하였습니다."; }
}
