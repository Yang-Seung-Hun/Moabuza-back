package com.project.moabuja.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // Token Error
    ACCESS_NOT_VALID(400, "T001", "Access Token이 유효하지 않습니다."),
    REFRESH_NOT_VALID(400, "T002", "Refresh Token이 유효하지 않습니다."),
    REFRESH_NOT_EXIST(400, "T003", "Refresh Token이 존재하지 않습니다."),
    REFRESH_NOT_MATCH(400, "T004", "Refresh Token이 일치하지 않습니다."),
    LOGOUT_TOKEN_VALID(400, "T004", "로그아웃 되어 사용할 수 없는 토큰입니다."),

    // Member Error
    MEMBER_NOT_FOUND(404, "M001", "해당 사용자는 존재하지 않습니다."),
    GEUST_TO_LOGIN(403, "M002", "Move to Login Page"),

    // Friend Error

    // Record Error
    RECORD_NOT_EXIST(404, "R001", "해당 기록은 존재하지 않습니다."),
    RECORD_MEMBER_NOT_MATCH(400, "R002", "해당 기록의 사용자가 아닙니다."),
    MONEY_LESS_THAN_WALLET(403, "R003", "지갑에 있는 돈보다 큰 돈을 사용할 수 없습니다."),
    SAVINGS_LESS_THAN_WALLET(403, "R004", "지갑에 있는 돈보다 큰 돈을 저금할 수 없습니다."),
    CHALLENGE_GOAL_NOT_EXIST(404, "R005", "생성된 도전해부자 없습니다.."),
    GROUP_GOAL_NOT_EXIST(404, "R006", "생성된 같이해부자 없습니다.."),

    // Goal Error
    GOAL_NOT_EXIST(404, "G001", "해당 목표는 존재하지 않습니다."),

    // Alarm Error
    ALARM_NOT_EXIST(404, "A001", "해당 알람은 존재하지 않습니다."),
    ALARM_MEMBER_NOT_MATCH(400, "A002", "해당 알람의 사용자가 아닙니다.");


    private final int status;
    private final String errorCode;
    private final String errorMessage;
}
