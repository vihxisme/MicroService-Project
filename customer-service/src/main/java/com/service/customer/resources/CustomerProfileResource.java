package com.service.customer.resources;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class CustomerProfileResource {
  private final UUID id;
  private final String customerCode;
  private final String firstName;
  private final String lastName;
  private final String gender;
  private final String email;
  private final String phone;
}
