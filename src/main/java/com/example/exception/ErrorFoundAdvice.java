package com.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorFoundAdvice {

    @ExceptionHandler(ErrorFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String nullExceptionHandler(ErrorFoundException ex) {
        return ex.getMessage();
    }
}
