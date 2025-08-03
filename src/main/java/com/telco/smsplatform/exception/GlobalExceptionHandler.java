package com.telco.smsplatform.customer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Handle @Valid errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getAllErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .findFirst()
                .orElse("Invalid request");

        return new ResponseEntity<>(
                "STATUS: REJECTED~~RESPONSE_CODE: FAILURE~~" + errorMessage,
                HttpStatus.BAD_REQUEST
        );
    }

    // Handle generic exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericExceptions(Exception ex) {
        return new ResponseEntity<>(
                "STATUS: REJECTED~~RESPONSE_CODE: FAILURE~~" + ex.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }
}

