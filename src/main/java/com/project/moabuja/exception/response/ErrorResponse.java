package com.project.moabuja.exception.response;

import lombok.*;

@Builder
@NoArgsConstructor
@Data
public class ErrorResponse {

    // 에러는 데이터를 주지 않는다.
    private String message;
    private String exceptionType; // 예외 타입

    @Builder
    public ErrorResponse(String message) {
        this.message = message;
    }

    @Builder
    public ErrorResponse(String message, String exceptionType) {
        this.message = message;
        this.exceptionType = exceptionType;
    }
}
