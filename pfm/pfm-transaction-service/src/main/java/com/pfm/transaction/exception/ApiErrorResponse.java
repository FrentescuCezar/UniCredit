package com.pfm.transaction.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ApiErrorResponse {
    private int status;
    private String message;
    private LocalDateTime timestamp;
    private List<String> validationErrors; // A list to hold detailed validation errors

    // Constructor for general errors
    public ApiErrorResponse(HttpStatus status, String message) {
        this.status = status.value();
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    // Constructor for validation errors
    public ApiErrorResponse(HttpStatus status, String message, List<String> validationErrors) {
        this.status = status.value();
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.validationErrors = validationErrors;
    }
}