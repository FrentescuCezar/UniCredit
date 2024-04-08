package com.pfm.category.exception;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ApiErrorResponse {
    private HttpStatus status;
    private String message;
    private LocalDateTime timestamp;

    public ApiErrorResponse(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

}
