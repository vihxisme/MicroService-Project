package com.service.customer.requests;

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
@Getter
@Setter
@Builder
public class AddAddressRequest {

    @NotNull
    private UUID customerID;

    @NotBlank
    private String name;

    @NotBlank
    private String phone;

    @NotBlank
    private String address;

    @NotBlank
    private Integer ward;

    @NotBlank
    private Integer district;

    @NotBlank
    private Integer province;

    @NotBlank
    private String fullAddress;

    @NotBlank
    @Builder.Default
    private String country = "Việt Nam";

    @Builder.Default
    private Boolean isDefault = false;
}
