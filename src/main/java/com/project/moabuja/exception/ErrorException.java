package com.project.moabuja.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ErrorException extends RuntimeException{
    private final ErrorCode errorCode;

    public ErrorException(String s, String message, ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
