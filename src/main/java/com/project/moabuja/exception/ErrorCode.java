package com.project.moabuja.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // Token Error
    REFRESH_NOT_VALID(HttpStatus.BAD_REQUEST, "400_Token_1", "Refresh Token이 유효하지 않습니다."),
    REFRESH_NOT_EXIST(HttpStatus.BAD_REQUEST, "404_Token_2", "Refresh Token이 존재하지 않습니다."),
    REFRESH_NOT_MATCH(HttpStatus.BAD_REQUEST, "400_Token_3", "Refresh Token이 일치하지 않습니다."),
    ACCESS_NOT_VALID(HttpStatus.BAD_REQUEST, "400_Token_4", "Access Token이 유효하지 않습니다."),

    // Member Error
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "404_Member_1", "해당 사용자는 존재하지 않습니다."),
    GEUST_TO_LOGIN(HttpStatus.FORBIDDEN, "403_Member_2", "Move to Login Page"),

    // Friend Error

    // Record Error

    // Goal Error

    // Alarm Error
    ALARM_NOT_EXITS(HttpStatus.NOT_FOUND, "404_Alarm_1", "해당 알람은 존재하지 않습니다."),
    ALARM_MEMBER_NOT_MATCH(HttpStatus.BAD_REQUEST, "400_Alarm_2", "해당 알람의 사용자가 아닙니다.");


    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String errorMessage;
}
