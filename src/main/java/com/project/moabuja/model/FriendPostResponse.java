package com.project.moabuja.model;

import lombok.Getter;

@Getter
public class FriendPostResponse {
    private final String msg;

    public FriendPostResponse(){
        this.msg = "친구 요청을 보냈습니다.";
    }
}
