package com.academy.auth.dto;

import lombok.Data;

@Data
public class ErrorResponse {

    private String message;
    private String details;

    private int statusCode;

    public ErrorResponse(String message, String details, int statusCode) {
        this.message = message;
        this.details = details;
        this.statusCode = statusCode;
    }
}
