package com.service.auth.responses;

import java.util.Map;

public class ErrorResponse {
  private final String massage;
  private final Map<String, String> errors;

  public ErrorResponse(String massage, Map<String, String> errors) {
    this.massage = massage;
    this.errors = errors;
  }

  public String getMassage() {
    return massage;
  }

  public Map<String, String> getErrors() {
    return errors;
  }
}
