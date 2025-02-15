package com.service.auth.requests;

public class ValidateRequest {
  private String token;

  public ValidateRequest() {
  }

  public ValidateRequest(String token) {
    this.token = token;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

}
