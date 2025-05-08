package com.service.order.resources;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Builder
public class OrderStatisticsResource {

    private final Long totalOrders;
    private final BigDecimal totalRevenue;
    private final Long pendingOrders;
    private final Long cancelledOrders;
}
