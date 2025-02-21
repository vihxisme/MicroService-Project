package com.service.auth.requests;

import java.util.UUID;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AddInfoCustomerRequest {
  @NotNull
  private UUID authUserId;

  @NotBlank
  private String firstName;

  @NotBlank
  private String lastName;

  @Email
  @NotBlank
  private String email;

  @NotBlank
  private String phone;

  public AddInfoCustomerRequest() {
  }

  public AddInfoCustomerRequest(@NotNull UUID authUserId, @NotBlank String firstName, @NotBlank String lastName,
      @Email @NotBlank String email, @NotBlank String phone) {
    this.authUserId = authUserId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.phone = phone;
  }

  public UUID getAuthUserId() {
    return authUserId;
  }

  public void setAuthUserId(UUID authUserId) {
    this.authUserId = authUserId;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

}
