package com.service.auth.dtos;

import jakarta.validation.constraints.NotBlank;

public class InfoCustomer {

  @NotBlank
  private String firstName;

  @NotBlank
  private String lastName;

  @NotBlank
  private String phone;

  public InfoCustomer() {
  }

  public InfoCustomer(@NotBlank String firstName, @NotBlank String lastName,
      @NotBlank String phone) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.phone = phone;
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

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

}
