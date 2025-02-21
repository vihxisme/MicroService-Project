package com.service.customer.responses;

import java.util.Map;

public class ErrorResponse {
  private final int code;
  private final String massage;
  private final Map<String, String> errors;

  public ErrorResponse(int code, String massage, Map<String, String> errors) {
    this.code = code;
    this.massage = massage;
    this.errors = errors;
  }

  public String getMassage() {
    return massage;
  }

  public Map<String, String> getErrors() {
    return errors;
  }

  public int getCode() {
    return code;
  }
}
