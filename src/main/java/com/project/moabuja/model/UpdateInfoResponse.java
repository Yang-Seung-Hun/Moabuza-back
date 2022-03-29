package com.project.moabuja.model;

import lombok.Getter;

@Getter
public class UpdateInfoResponse {
    private final String msg;

    public UpdateInfoResponse(){ this.msg = "캐릭터, 닉네임 설정이 완료되었습니다."; }
}
