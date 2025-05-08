package com.service.order.resources;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Builder
public class TransactionResource {

    private final BigDecimal totalSpent;
    private final Long successfulTransactions;
}
