package com.project.moabuja.model;

import lombok.Getter;

@Getter
public class GroupAcceptResponse {
    private final String msg;

    public GroupAcceptResponse(){
        this.msg = "같이해부자 요청을 수락하였습니다.";
    }
}
