package com.service.apigateway.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ValidateRequest {
  private String token;

  public ValidateRequest() {
  }

  public ValidateRequest(String token) {
    this.token = token;
  }

  @JsonProperty("token")
  public String getToken() {
    return token;
  }

  @JsonProperty("token")
  public void setToken(String token) {
    this.token = token;
  }
}
