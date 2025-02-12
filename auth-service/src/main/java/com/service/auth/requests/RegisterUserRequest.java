package com.service.auth.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterUserRequest {
  @NotBlank(message = "Username is required")
  private String username;

  @Email
  @NotBlank(message = "Email is required")
  private String email;

  @NotBlank(message = "Password is required")
  @Size(min = 6, max = 16, message = "Password must be between 6 and 16 characters")
  private String password;

  @NotBlank(message = "Confirm Password is required")
  @Size(min = 6, max = 16, message = "Password must be between 6 and 16 characters")
  private String confirmPassword;

  public RegisterUserRequest() {
  }

  public RegisterUserRequest(String username, String email, String password, String confirmPassword) {
    this.username = username;
    this.email = email;
    this.password = password;
    this.confirmPassword = confirmPassword;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getConfirmPassword() {
    return confirmPassword;
  }

  public void setConfirmPassword(String confirmPassword) {
    this.confirmPassword = confirmPassword;
  }
}
