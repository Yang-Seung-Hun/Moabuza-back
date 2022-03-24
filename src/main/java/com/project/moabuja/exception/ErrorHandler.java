package com.project.moabuja.exception;

import com.project.moabuja.exception.exceptionClass.AlarmErrorException;
import com.project.moabuja.exception.exceptionClass.MemberNotFoundException;
import com.project.moabuja.exception.exceptionClass.LogoutJwtUseException;
import com.project.moabuja.exception.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodArgumentNotValidException(MethodArgumentNotValidException e){
        return new ResponseEntity<>(ErrorResponse.badRequest("ValidException"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LogoutJwtUseException.class)
    public ResponseEntity<ErrorResponse> jwtExpiredException(LogoutJwtUseException e){
        return new ResponseEntity<>(ErrorResponse.badRequest(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<ErrorResponse> memberNotFoundException(MemberNotFoundException e) {
        return new ResponseEntity<>(ErrorResponse.badRequest(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AlarmErrorException.class)
    public ResponseEntity<ErrorResponse> alarmErrorException(AlarmErrorException e) {
        return new ResponseEntity<>(ErrorResponse.badRequest(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

}
