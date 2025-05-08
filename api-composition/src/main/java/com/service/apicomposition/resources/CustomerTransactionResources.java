package com.service.apicomposition.resources;

import java.math.BigDecimal;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerTransactionResources {

    @JsonProperty("id")
    private final UUID id;

    @JsonProperty("customerCode")
    private final String customerCode;

    @JsonProperty("firstName")
    private final String firstName;

    @JsonProperty("lastName")
    private final String lastName;

    @JsonProperty("gender")
    private final String gender;

    @JsonProperty("email")
    private final String email;

    @JsonProperty("phone")
    private final String phone;

    @JsonProperty("avatar")
    private final String avatar;

    @JsonProperty("totalSpent")
    private final BigDecimal totalSpent;

    @JsonProperty("successfulTransactions")
    private final Long successfulTransactions;

    @JsonCreator
    public CustomerTransactionResources(
            @JsonProperty("id") UUID id,
            @JsonProperty("customerCode") String customerCode,
            @JsonProperty("firstName") String firstName,
            @JsonProperty("lastName") String lastName,
            @JsonProperty("gender") String gender,
            @JsonProperty("email") String email,
            @JsonProperty("phone") String phone,
            @JsonProperty("avatar") String avatar,
            @JsonProperty("totalSpent") BigDecimal totalSpent,
            @JsonProperty("successfulTransactions") Long successfulTransactions
    ) {
        this.id = id;
        this.customerCode = customerCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
        this.avatar = avatar;
        this.totalSpent = totalSpent;
        this.successfulTransactions = successfulTransactions;
    }
}
