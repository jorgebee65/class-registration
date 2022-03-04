package com.jorge.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ApiRequestException.class)
    public ResponseEntity<Object> handleApiRequestException(ApiRequestException e){
        ApiException apiException = new ApiException(e.getMessage(), HttpStatus.BAD_REQUEST, ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity(apiException, HttpStatus.BAD_REQUEST);
    }
}
