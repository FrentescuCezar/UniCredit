package com.pfm.category.exception;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
@Builder
public class ApiErrorResponse {
    private int status;
    private String message;
    private LocalDateTime timestamp;
    private List<String> validationErrors;
}

