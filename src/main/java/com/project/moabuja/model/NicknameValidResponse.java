package com.project.moabuja.model;

import lombok.Getter;

@Getter
public class NicknameValidResponse {
    private final String msg;

    public NicknameValidResponse(String nickname){ this.msg = "해당 닉네임은 사용 중 입니다."; }

    public NicknameValidResponse(){ this.msg = "해당 닉네임은 사용이 가능합니다."; }
}
