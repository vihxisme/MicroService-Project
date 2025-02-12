package com.service.auth.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AddUserRequest {
  @NotBlank(message = "Username is required")
  private String username;

  @NotBlank(message = "Email is required")
  @Email
  private String email;

  @NotBlank(message = "Password is required")
  @Size(min = 6, max = 16, message = "Password must be between 6 and 16 characters")
  private String password;

  private String role;

  public AddUserRequest() {
  }

  public AddUserRequest(String username, String email, String password, String role) {
    this.username = username;
    this.email = email;
    this.password = password;
    this.role = role;
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

  public String getRole() {
    return role;
  }

  public void setString(String role) {
    this.role = role;
  }
}
