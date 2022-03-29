package com.project.moabuja.exception;

import lombok.*;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {

    private int status;
    private String errorName;
    private String errorCode;
    private String msg;

    @Builder
    public ErrorResponse(int status, String errorName, String errorCode, String msg) {
        this.status = status;
        this.errorName = errorName;
        this.errorCode = errorCode;
        this.msg = msg;
    }

    public static ResponseEntity<ErrorResponse> of(ErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ErrorResponse.builder()
                        .status(errorCode.getStatus())
                        .errorName(errorCode.name())
                        .errorCode(errorCode.getErrorCode())
                        .msg(errorCode.getErrorMessage())
                        .build()
                );
    }
}

//    private String message;
//    private int statusCode;
//
//    @Builder
//    public ErrorResponse(String message) {
//        this.message = message;
//    }
//
//    public static ErrorResponse badRequest(String message){
//        return new ErrorResponse(message, HttpStatus.BAD_REQUEST.value());
//    }
//
//    public static ErrorResponse forbidden(String message){
//        return new ErrorResponse(message, HttpStatus.FORBIDDEN.value());
//    }
//
//    public static ErrorResponse notFound(String message){
//        return new ErrorResponse(message, HttpStatus.NOT_FOUND.value());
//    }
//
//    public static ErrorResponse unauthorized(String message){
//        return new ErrorResponse(message, HttpStatus.UNAUTHORIZED.value());
//    }