package com.github.explore_with_me.main.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Getter
public class ApiError {
    private HttpStatus status;
    private String message;
    private List<String> errors;
    private String reason;
    private LocalDateTime timestamp;

    public ApiError(HttpStatus status, String message, List<String> errors, String reason) {
        this.status = status;
        this.message = message;
        this.errors = errors;
        this.reason = reason;
        this.timestamp = LocalDateTime.now();

    }

    public ApiError(HttpStatus status, String message, String error, String reason) {
        this.status = status;
        this.message = message;
        errors = Arrays.asList(error);
        this.reason = reason;
        this.timestamp = LocalDateTime.now();
    }
}
