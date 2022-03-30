package com.project.moabuja.model;

import lombok.Getter;

@Getter
public class GroupCreateResponse {
    private final String msg;

    public GroupCreateResponse(){
        this.msg = "도전해부자 생성을 완료하였습니다.";
    }
}
