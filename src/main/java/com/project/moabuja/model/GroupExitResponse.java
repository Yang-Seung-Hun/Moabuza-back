package com.project.moabuja.model;

import lombok.Getter;

@Getter
public class GroupExitResponse {
    private final String msg;

    public GroupExitResponse(){ this.msg = "같이해부자 나가기 완료 되었습니다."; }
}
