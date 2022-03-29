package com.project.moabuja.model;

import lombok.Getter;

@Getter
public class ChallengePostResponse {
    private final String msg;

    public ChallengePostResponse(){
        this.msg = "도전해부자 요청을 보냈습니다.";
    }
}
