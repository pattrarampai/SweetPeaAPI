package com.sweetpeatime.sweetpeatime.common.exception;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommonExceptionResponse {
    private String message;
    private LocalDateTime timestamp;
    public CommonExceptionResponse(String message) {
        super();
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}
