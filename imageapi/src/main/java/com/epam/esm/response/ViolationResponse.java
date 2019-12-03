package com.epam.esm.response;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class ViolationResponse {

    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
}
