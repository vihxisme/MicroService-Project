package com.service.auth.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginRequest {

  @NotBlank(message = "Identifier is required")
  private String identifier;

  @NotBlank(message = "Password is required")
  @Size(min = 6, max = 16, message = "Password must be between 6 and 16 characters")
  private String password;

  public LoginRequest() {
  }

  public LoginRequest(String identifier, String password) {
    this.identifier = identifier;
    this.password = password;
  }

  public String getIdentifier() {
    return identifier;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
