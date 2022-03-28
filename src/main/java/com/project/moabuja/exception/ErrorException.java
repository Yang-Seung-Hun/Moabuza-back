package com.project.moabuja.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ErrorException extends RuntimeException{
    private final ErrorManual errorManual;

    public ErrorException(String s, String message, ErrorManual errorManual) {
        this.errorManual = errorManual;
    }
}
