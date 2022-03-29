package com.project.moabuja.model;

import lombok.Getter;

@Getter
public class FriendAcceptResponse {
    private final String msg;

    public FriendAcceptResponse(){
        this.msg = "친구 요청을 수락하였습니다.";
    }
}
