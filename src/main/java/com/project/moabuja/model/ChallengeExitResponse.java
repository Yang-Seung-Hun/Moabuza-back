package com.project.moabuja.model;

import lombok.Getter;

@Getter
public class ChallengeExitResponse {
    private final String msg;

    public ChallengeExitResponse(){ this.msg = "도전해부자 나가기 완료 되었습니다."; }
}
