package com.project.moabuja.model;

import lombok.Getter;

@Getter
public class LogoutResponse {
    private final String msg;

    public LogoutResponse(){ this.msg = "로그아웃이 완료되었습니다."; }
}
