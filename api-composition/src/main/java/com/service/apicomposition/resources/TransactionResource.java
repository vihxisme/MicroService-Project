package com.service.apicomposition.resources;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class TransactionResource {

    private final BigDecimal totalSpent;
    private final Long successfulTransactions;
}
