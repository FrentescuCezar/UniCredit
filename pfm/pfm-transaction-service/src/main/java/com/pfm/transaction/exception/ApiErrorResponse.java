package com.pfm.transaction.exception;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ApiErrorResponse {
    private int status;
    private String message;
    private LocalDateTime timestamp;
    private List<String> validationErrors;
}