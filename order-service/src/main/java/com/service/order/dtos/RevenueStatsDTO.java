package com.service.order.dtos;

import java.math.BigDecimal;

public interface RevenueStatsDTO {

    String getLabel();

    BigDecimal getTotalRevenue();

    Long getOrderCount();

    Double getGrowthPercent();
}
