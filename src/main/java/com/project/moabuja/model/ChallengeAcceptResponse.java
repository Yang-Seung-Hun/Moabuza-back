package com.project.moabuja.model;

import lombok.Getter;

@Getter
public class ChallengeAcceptResponse {
    private final String msg;

    public ChallengeAcceptResponse(){
        this.msg = "도전해부자 요청을 수락하였습니다.";
    }
}
