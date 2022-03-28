package com.project.moabuja.exception;

import com.project.moabuja.exception.exceptionClass.AlarmErrorException;
import com.project.moabuja.exception.exceptionClass.MemberNotFoundException;
import com.project.moabuja.exception.exceptionClass.LogoutJwtUseException;
import com.project.moabuja.exception.exceptionClass.RecordErrorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(value = {ErrorException.class})
    protected ResponseEntity<ErrorResponse> customException(ErrorException e) {
        log.error("Error : " + e.getErrorManual());
        return ErrorResponse.toResponseEntity(e.getErrorManual());
    }
//
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ErrorResponse> methodArgumentNotValidException(MethodArgumentNotValidException e){
//        return new ResponseEntity<>(ErrorResponse.badRequest("ValidException"), HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler(LogoutJwtUseException.class)
//    public ResponseEntity<ErrorResponse> jwtExpiredException(LogoutJwtUseException e){
//        return new ResponseEntity<>(ErrorResponse.badRequest(e.getMessage()), HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler(MemberNotFoundException.class)
//    public ResponseEntity<ErrorResponse> memberNotFoundException(MemberNotFoundException e) {
//        return new ResponseEntity<>(ErrorResponse.badRequest(e.getMessage()), HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler(AlarmErrorException.class)
//    public ResponseEntity<ErrorResponse> alarmErrorException(AlarmErrorException e) {
//        return new ResponseEntity<>(ErrorResponse.badRequest(e.getMessage()), HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler(RecordErrorException.class)
//    public ResponseEntity<ErrorResponse> recordErrorException(RecordErrorException e) {
//        return new ResponseEntity<>(ErrorResponse.badRequest(e.getMessage()), HttpStatus.BAD_REQUEST);
//    }

}
