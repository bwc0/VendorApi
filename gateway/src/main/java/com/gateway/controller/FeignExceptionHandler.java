package com.gateway.controller;

import feign.FeignException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class FeignExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(FeignException.class)
    public ResponseEntity<Object> fiegnExceptionHandler(FeignException fe, WebRequest request) {
        return new ResponseEntity<>(fe.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND);
    }
}
