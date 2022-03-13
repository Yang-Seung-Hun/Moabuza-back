package com.project.moabuja.exception;

import com.project.moabuja.exception.exceptionClass.JwtExpiredException;
import com.project.moabuja.exception.exceptionClass.LogoutJwtUseException;
import com.project.moabuja.exception.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler {

    private ErrorResponse response;

//    @ExceptionHandler(LogoutJwtUseException.class)
//    public ResponseEntity jwtExpiredException(LogoutJwtUseException e){
//        response = ErrorResponse.builder()
//                .message(e.getMessage())
//                .build();
//        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//    }

}
