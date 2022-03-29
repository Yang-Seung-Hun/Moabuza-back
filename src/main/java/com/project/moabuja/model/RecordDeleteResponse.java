package com.project.moabuja.model;

import lombok.Getter;

@Getter
public class RecordDeleteResponse {
    private final String msg;

    public RecordDeleteResponse(){
        this.msg = "내역이 삭제 되었습니다.";
    }
}
