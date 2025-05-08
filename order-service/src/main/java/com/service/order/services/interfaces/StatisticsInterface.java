package com.service.order.services.interfaces;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.service.order.dtos.GrowthResult;
import com.service.order.dtos.OrderRevenueStatsDTO;
import com.service.order.dtos.OrderStatusCountInterfaceDTO;
import com.service.order.dtos.OrderStatusStatsDTO;
import com.service.order.dtos.RevenueByRangeTypeDTO;
import com.service.order.dtos.RevenueStatsDTO;
import com.service.order.requests.PaginationRequest;
import com.service.order.resources.OrderMnResource;
import com.service.order.responses.PaginationResponse;

public interface StatisticsInterface {

    BigDecimal totalRevenuee();

    BigDecimal totalRevenueByUserId();

    Long countCustomer();

    Long countNewOrder();

    List<RevenueStatsDTO> getDashboardStats();

    List<OrderStatusCountInterfaceDTO> getStats(String rangeType);

    List<RevenueByRangeTypeDTO> getRevenueByRangeType(String rangeType);

    GrowthResult<OrderRevenueStatsDTO> getRevenueStatsByRangeType(String rangeType);

    GrowthResult<OrderStatusStatsDTO> getOrderStatsByRangeType(String rangeType);

    GrowthResult<OrderRevenueStatsDTO> getTopProductRevenueBetween(String rangeType);

    GrowthResult<OrderStatusStatsDTO> getNewCustomerStatsByRangeType(String rangeType);

    PaginationResponse<OrderMnResource> getPagiOrderByRangeType(PaginationRequest request, String rangeType);

    Map<UUID, BigDecimal> getTopProductByRangeType(Integer limit, String rangeType);
}
