package com.service.customer.requests;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class UpdateAddrerssRequest {
  @NotNull
  private Long id;

  private UUID customerID;

  @NotBlank
  private String name;

  @NotBlank
  private String phone;

  @NotBlank
  private String street;

  @NotBlank
  private String state;

  @NotBlank
  private String city;

  @NotBlank
  private String country;

  @Builder.Default
  private Boolean isDefault = false;
}
