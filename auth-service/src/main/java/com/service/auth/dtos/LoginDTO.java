package com.service.auth.dtos;

import java.util.UUID;

public class LoginDTO {
  private UUID id;
  private String username;
  private String email;
  private String role;

  public LoginDTO() {
  }

  public LoginDTO(UUID id, String username, String email, String role) {
    this.id = id;
    this.username = username;
    this.email = email;
    this.role = role;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
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

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }
}
