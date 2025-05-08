package com.service.customer.requests;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class UpdateInfoCustomerRequest {

    @NotNull
    private UUID authUserId;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    private String gender;

    private LocalDate dob;

    private String avatar;
}
