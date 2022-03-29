package com.project.moabuja.model;

import lombok.Getter;

@Getter
public class GroupRefuseResponse {
    private final String msg;

    public GroupRefuseResponse(){ this.msg = "같이해부자 요청을 거절하였습니다."; }
}
