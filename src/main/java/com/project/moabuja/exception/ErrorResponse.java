package com.project.moabuja.exception;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Getter
@Builder
public class ErrorResponse {

    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int status;
    private final String error;
    private final String errorCode;
    private final String msg;

    public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorManual errorManual) {
        return ResponseEntity
                .status(errorManual.getHttpStatus().value())
                .body(ErrorResponse.builder()
                    .error(errorManual.getHttpStatus().name())
                    .errorCode(errorManual.getErrorCode())
                    .msg(errorManual.getErrorMessage())
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