package com.service.cart.responses;

import java.util.Map;

public class ErrorResponse {

    private final int code;
    private final String message;
    private final Map<String, String> errors;

    public ErrorResponse(int code, String message, Map<String, String> errors) {
        this.code = code;
        this.message = message;
        this.errors = errors;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

}
