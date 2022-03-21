package com.project.moabuja.exception.response;

import lombok.*;
import org.springframework.http.HttpStatus;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ErrorResponse {

    private String message;
    private int statusCode;

    @Builder
    public ErrorResponse(String message) {
        this.message = message;
    }

    public static ErrorResponse badRequest(String message){
        return new ErrorResponse(message, HttpStatus.BAD_REQUEST.value());
    }

    public static ErrorResponse forbidden(String message){
        return new ErrorResponse(message, HttpStatus.FORBIDDEN.value());
    }

    public static ErrorResponse notFound(String message){
        return new ErrorResponse(message, HttpStatus.NOT_FOUND.value());
    }

    public static ErrorResponse unauthorized(String message){
        return new ErrorResponse(message, HttpStatus.UNAUTHORIZED.value());
    }

}
