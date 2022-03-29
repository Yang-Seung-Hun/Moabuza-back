package com.project.moabuja.model;

import lombok.Getter;

@Getter
public class GroupPostResponse {
    private final String msg;

    public GroupPostResponse(){
        this.msg = "같이해부자 요청을 보냈습니다.";
    }
}
