package com.github.oleksii.zinkevych.currency_exchange_point.controller;

import com.github.oleksii.zinkevych.currency_exchange_point.service.exception.NotFoundApplicationServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundApplicationServiceException.class)
    public ResponseEntity<String> notFound(NotFoundApplicationServiceException e) {
        LOGGER.warn("Exception: {}", e.getMessage());
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .contentType(MediaType.TEXT_PLAIN)
            .body(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> notValid(MethodArgumentNotValidException e) {
        LOGGER.warn("Exception: {}", e.getMessage());
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .contentType(MediaType.TEXT_PLAIN)
            .body(e.getBindingResult()
                .getAllErrors()
                .get(0)
                .getDefaultMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        LOGGER.error("Exception: ", e);
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(MediaType.TEXT_PLAIN)
            .body("Internal server error");
    }
}
