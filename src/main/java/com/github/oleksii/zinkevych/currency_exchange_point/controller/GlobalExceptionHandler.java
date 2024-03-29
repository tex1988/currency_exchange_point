package com.github.oleksii.zinkevych.currency_exchange_point.controller;

import java.util.HashMap;
import java.util.Map;

import com.github.oleksii.zinkevych.currency_exchange_point.service.exception.ApplicationServiceNotConfirmedException;
import com.github.oleksii.zinkevych.currency_exchange_point.service.exception.ApplicationServiceNotFoundException;
import com.github.oleksii.zinkevych.currency_exchange_point.service.exception.ExchangeServiceNotFoundException;
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

    private static final String INTERNAL_SERVER_ERROR_MESSAGE = "Internal server error";

    @ExceptionHandler(ApplicationServiceNotFoundException.class)
    public ResponseEntity<Map<String, ?>> applicationNotFound(ApplicationServiceNotFoundException e) {
        return getNotFoundResponseEntity(e.getMessage());
    }

    @ExceptionHandler(ExchangeServiceNotFoundException.class)
    public ResponseEntity<Map<String, ?>> exchangeNotFound(ExchangeServiceNotFoundException e) {
        return getNotFoundResponseEntity(e.getMessage());
    }

    private ResponseEntity<Map<String, ?>> getNotFoundResponseEntity(String message) {
        log.warn("Exception: {}", message);
        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("success", false);
        body.put("message", message);
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .contentType(MediaType.APPLICATION_JSON)
            .body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, ?>> notValid(MethodArgumentNotValidException e) {
        log.warn("Exception: {}", e.getMessage());
        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("success", false);
        body.put("message", e.getBindingResult()
            .getAllErrors()
            .get(0)
            .getDefaultMessage());
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .contentType(MediaType.APPLICATION_JSON)
            .body(body);
    }

    @ExceptionHandler(ApplicationServiceNotConfirmedException.class)
    public ResponseEntity<Map<String, ?>> notConfirmed(ApplicationServiceNotConfirmedException e) {
        log.warn("Exception: {}", e.getMessage());
        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.UNPROCESSABLE_ENTITY.value());
        body.put("success", false);
        body.put("message", e.getMessage());
        return ResponseEntity
            .status(HttpStatus.UNPROCESSABLE_ENTITY)
            .contentType(MediaType.APPLICATION_JSON)
            .body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, ?>> handleException(Exception e) {
        log.error("Exception: ", e);
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Map.of("status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "success", false,
                "message", INTERNAL_SERVER_ERROR_MESSAGE));
    }
}
