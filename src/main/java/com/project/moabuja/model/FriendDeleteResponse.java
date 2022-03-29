package com.project.moabuja.model;

import lombok.Getter;

@Getter
public class FriendDeleteResponse {
    private final String msg;

    public FriendDeleteResponse(){
        this.msg = "친구가 삭제 되었습니다.";
    }
}
