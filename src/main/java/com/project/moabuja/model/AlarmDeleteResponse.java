package com.project.moabuja.model;

import lombok.Getter;

@Getter
public class AlarmDeleteResponse {
    private final String msg;

    public AlarmDeleteResponse(){
        this.msg = "알람이 삭제 되었습니다.";
    }
}
