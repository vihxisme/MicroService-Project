package com.service.customer.resources;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Builder
public class CustomerTransactionResource {

    private final UUID id;
    private final String customerCode;
    private final String firstName;
    private final String lastName;
    private final String gender;
    private final String email;
    private final String phone;
    private final String avatart;
    private final BigDecimal totalSpent;
    private final Long successfulTransactions;
}
