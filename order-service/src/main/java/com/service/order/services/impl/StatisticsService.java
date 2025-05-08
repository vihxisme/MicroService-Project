package com.service.order.services.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.service.order.dtos.GrowthResult;
import com.service.order.dtos.OrderRevenueStatsDTO;
import com.service.order.dtos.OrderStatusCountImplDTO;
import com.service.order.dtos.OrderStatusCountInterfaceDTO;
import com.service.order.dtos.OrderStatusStatsDTO;
import com.service.order.dtos.RevenueByRangeTypeDTO;
import com.service.order.dtos.RevenueStatsDTO;
import com.service.order.dtos.TopProductDTO;
import com.service.order.repositories.StatisticsRepository;
import com.service.order.requests.PaginationRequest;
import com.service.order.resources.OrderMnResource;
import com.service.order.responses.PaginationResponse;
import com.service.order.services.interfaces.StatisticsInterface;

@Service
public class StatisticsService implements StatisticsInterface {

    @Autowired
    private StatisticsRepository statisticsRepository;

    private Logger logger = LoggerFactory.getLogger(StatisticsService.class);

    /**
     * Lấy thống kê doanh thu cho bảng điều khiển, bao gồm các khoảng thời gian
     * như hôm nay, tuần này, tháng này, quý này, và năm nay.
     *
     * Phương thức này: - Xây dựng các khoảng thời gian cần thiết bằng cách gọi
     * phương thức buildTimeRanges(). - Duyệt qua các khoảng thời gian và tính
     * toán doanh thu cho mỗi khoảng thời gian hiện tại và doanh thu của khoảng
     * thời gian trước đó. - Lấy dữ liệu thống kê từ repository (thông qua
     * phương thức statisticsRepository.getStatsBetween()). - Trả về danh sách
     * các đối tượng RevenueStatsDTO chứa thông tin thống kê cho các khoảng thời
     * gian.
     *
     * @return List<RevenueStatsDTO> Danh sách thống kê doanh thu cho các khoảng
     * thời gian khác nhau. * Fetches revenue statistics for the dashboard,
     * covering time ranges such as today, this week, this month, this quarter,
     * and this year.
     *
     * This method: - Builds the necessary time ranges by calling the
     * buildTimeRanges() method. - Iterates through the time ranges and
     * calculates the revenue for each current time range and the previous
     * period. - Retrieves statistical data from the repository (via the
     * statisticsRepository.getStatsBetween() method). - Returns a list of
     * RevenueStatsDTO objects containing the statistics for the time ranges.
     *
     * @return List<RevenueStatsDTO> A list of revenue statistics for different
     * time ranges.
     */
    @Override
    public List<RevenueStatsDTO> getDashboardStats() {
        List<RevenueStatsDTO> results = new ArrayList<>();

        results.add(getStatsWithGrowth("Ngày", Duration.ofDays(1)));
        results.add(getStatsWithGrowth("Tuần", Duration.ofDays(7)));
        results.add(getStatsWithGrowth("Tháng", Period.ofMonths(1)));
        results.add(getStatsWithGrowth("Quý", Period.ofMonths(3)));
        results.add(getStatsWithGrowth("Năm", Period.ofYears(1)));

        return results;
    }

    private RevenueStatsDTO getStatsWithGrowth(String label, TemporalAmount period) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.minus(period);
        LocalDateTime previousStart = start.minus(period);
        LocalDateTime previousEnd = start;

        BigDecimal prevRevenue = statisticsRepository.sumTotalRevenue(previousStart, previousEnd);
        if (prevRevenue == null) {
            prevRevenue = BigDecimal.ZERO;
        }

        return statisticsRepository.getStatsBetween(label, start, now, prevRevenue);
    }

    @Override
    public BigDecimal totalRevenueByUserId() {
        return statisticsRepository.totalRevenueByUserId();
    }

    /**
     * Calculates and retrieves the total revenue of the entire store.
     *
     * @return the total revenue as a BigDecimal.
     */
    @Override
    public BigDecimal totalRevenuee() {
        return statisticsRepository.totalRevenue();
    }

    @Override
    public Long countCustomer() {
        return statisticsRepository.countCustomer();
    }

    /**
     * Counts the number of orders that are in the "pending" status.
     *
     * @return the total number of pending orders.
     */
    @Override
    public Long countNewOrder() {
        return statisticsRepository.countNewOrder();
    }

    /**
     * Retrieves the count of orders by status for a specified time range.
     *
     * This method calls the `getRange()` utility method from the
     * `DateRangeUtil` class to obtain the start and end times for the specified
     * time range (`rangeType`). It then uses the
     * `statisticsRepository.countOrdersByStatusBetween()` method to retrieve
     * the count of orders by their status within the given time range.
     *
     * The `rangeType` parameter specifies the time period for which the
     * statistics are needed. Valid range types could include: - "day": Today's
     * orders. - "week": Orders within the current week. - "month": Orders
     * within the current month. - "quarter": Orders within the current quarter.
     * - "year": Orders within the current year.
     *
     * The method returns a list of `OrderStatusCountDTO` objects containing the
     * order status counts.
     *
     * @param rangeType The type of time range to retrieve statistics for (e.g.,
     * day, week, month, quarter, year).
     * @return List<OrderStatusCountDTO> A list of `OrderStatusCountDTO` objects
     * containing the count of orders by status.
     */
    @Override
    public List<OrderStatusCountInterfaceDTO> getStats(String rangeType) {
        switch (rangeType.toLowerCase()) {
            case "day" -> {
                return getStatsOrderStatusCount(Duration.ofDays(1));
            }
            case "week" -> {
                return getStatsOrderStatusCount(Duration.ofDays(7));
            }
            case "month" -> {
                return getStatsOrderStatusCount(Period.ofMonths(1));
            }
            case "quarter" -> {
                return getStatsOrderStatusCount(Period.ofMonths(3));
            }
            case "year" -> {
                return getStatsOrderStatusCount(Period.ofYears(1));
            }
            default ->
                throw new IllegalArgumentException(String.format("Invalid Range: %s", rangeType));
        }
    }

    private List<OrderStatusCountInterfaceDTO> getStatsOrderStatusCount(TemporalAmount period) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.minus(period);

        List<OrderStatusCountInterfaceDTO> result = statisticsRepository.countOrdersByStatusBetween(
                start, now
        );

        // Khởi tạo map mặc định với tất cả status = 0
        Map<Integer, Long> statusMap = new HashMap<>();
        for (int status = 1; status <= 5; status++) {
            statusMap.put(status, 0L);
        }
        // Ghi đè các status có dữ liệu
        for (OrderStatusCountInterfaceDTO dto : result) {
            statusMap.put(dto.getStatus(), dto.getCount());
        }
        // Trả về danh sách đầy đủ
        return statusMap.entrySet().stream()
                .map(e -> new OrderStatusCountImplDTO(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }

    /**
     * Trả về danh sách doanh thu theo mốc thời gian được chỉ định.
     *
     * @param rangeType loại khoảng thời gian cần thống kê: - "day": doanh thu
     * theo từng giờ trong ngày hiện tại. - "week": doanh thu theo từng ngày
     * trong 7 ngày gần nhất. - "month": doanh thu theo từng tháng trong 12
     * tháng gần nhất. - "quarter": doanh thu theo từng quý trong 4 quý gần
     * nhất. - "year": doanh thu theo từng năm từ năm có bản ghi đầu tiên.
     * @return danh sách đối tượng RevenueByRangeTypeDTO tương ứng với khoảng
     * thời gian.
     * @throws IllegalArgumentException nếu rangeType không hợp lệ.
     */
    @Override
    public List<RevenueByRangeTypeDTO> getRevenueByRangeType(String rangeType) {
        switch (rangeType.toLowerCase()) {
            case "day" -> {
                return statisticsRepository.getRevenueByHour();
            }
            case "week" -> {
                return statisticsRepository.getRevenueByDay();
            }
            case "month" -> {
                return statisticsRepository.getRevenueByMonth();
            }
            case "quarter" -> {
                return statisticsRepository.getRevenueByQuarter();
            }
            case "year" -> {
                return statisticsRepository.getRevenueByYear();
            }
            default ->
                throw new IllegalArgumentException(String.format("Invalid Range: %s", rangeType));
        }
    }

    private GrowthResult<OrderRevenueStatsDTO> getStatsRevenueByPeriod(TemporalAmount period) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime currentStart = now.minus(period);
        LocalDateTime previousStart = currentStart.minus(period);
        LocalDateTime previousEnd = currentStart;

        // Current period
        OrderRevenueStatsDTO current = statisticsRepository.getStatsRevenueBetween(currentStart, now);
        OrderRevenueStatsDTO previous = statisticsRepository.getStatsRevenueBetween(previousStart, previousEnd);

        return new GrowthResult<OrderRevenueStatsDTO>(
                current,
                previous,
                calculateGrowth(previous.getTotalRevenue(), current.getTotalRevenue())
        );
    }

    private BigDecimal calculateGrowth(BigDecimal prev, BigDecimal curr) {
        if (prev == null || prev.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return curr.subtract(prev)
                .divide(prev, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }

    @Override
    public GrowthResult<OrderRevenueStatsDTO> getRevenueStatsByRangeType(String rangeType) {
        switch (rangeType.toLowerCase()) {
            case "day" -> {
                return getStatsRevenueByPeriod(Duration.ofDays(1));
            }
            case "week" -> {
                return getStatsRevenueByPeriod(Duration.ofDays(7));
            }
            case "month" -> {
                return getStatsRevenueByPeriod(Period.ofMonths(1));
            }
            case "quarter" -> {
                return getStatsRevenueByPeriod(Period.ofMonths(3));
            }
            case "year" -> {
                return getStatsRevenueByPeriod(Period.ofYears(1));
            }
            default ->
                throw new IllegalArgumentException(String.format("Invalid Range: %s", rangeType));
        }
    }

    private GrowthResult<OrderStatusStatsDTO> getStatsOrderByPeriod(TemporalAmount period) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime currentStart = now.minus(period);
        LocalDateTime previousStart = currentStart.minus(period);
        LocalDateTime previousEnd = currentStart;

        // Current period
        OrderStatusStatsDTO current = statisticsRepository.getStatsOrderBetween(currentStart, now);
        OrderStatusStatsDTO previous = statisticsRepository.getStatsOrderBetween(previousStart, previousEnd);

        return new GrowthResult<OrderStatusStatsDTO>(
                current,
                previous,
                calculateGrowth(BigDecimal.valueOf(previous.getTotalOrder()), BigDecimal.valueOf(current.getTotalOrder()))
        );
    }

    @Override
    public GrowthResult<OrderStatusStatsDTO> getOrderStatsByRangeType(String rangeType) {
        switch (rangeType.toLowerCase()) {
            case "day" -> {
                return getStatsOrderByPeriod(Duration.ofDays(1));
            }
            case "week" -> {
                return getStatsOrderByPeriod(Duration.ofDays(7));
            }
            case "month" -> {
                return getStatsOrderByPeriod(Period.ofMonths(1));
            }
            case "quarter" -> {
                return getStatsOrderByPeriod(Period.ofMonths(3));
            }
            case "year" -> {
                return getStatsOrderByPeriod(Period.ofYears(1));
            }
            default ->
                throw new IllegalArgumentException(String.format("Invalid Range: %s", rangeType));
        }
    }

    private GrowthResult<OrderRevenueStatsDTO> findTopProductRevenueBetween(TemporalAmount period) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime currentStart = now.minus(period);
        LocalDateTime previousStart = currentStart.minus(period);
        LocalDateTime previousEnd = currentStart;

        // Current period
        OrderRevenueStatsDTO current = statisticsRepository.findTopProductRevenueBetween(currentStart, now);
        OrderRevenueStatsDTO previous = statisticsRepository.findTopProductRevenueBetween(previousStart, previousEnd);

        return new GrowthResult<OrderRevenueStatsDTO>(
                current,
                previous,
                calculateGrowth(previous.getTotalRevenue(), current.getTotalRevenue())
        );
    }

    @Override
    public GrowthResult<OrderRevenueStatsDTO> getTopProductRevenueBetween(String rangeType) {
        switch (rangeType.toLowerCase()) {
            case "day" -> {
                return findTopProductRevenueBetween(Duration.ofDays(1));
            }
            case "week" -> {
                return findTopProductRevenueBetween(Duration.ofDays(7));
            }
            case "month" -> {
                return findTopProductRevenueBetween(Period.ofMonths(1));
            }
            case "quarter" -> {
                return findTopProductRevenueBetween(Period.ofMonths(3));
            }
            case "year" -> {
                return findTopProductRevenueBetween(Period.ofYears(1));
            }
            default ->
                throw new IllegalArgumentException(String.format("Invalid Range: %s", rangeType));
        }
    }

    private GrowthResult<OrderStatusStatsDTO> getStatsNewCustomerByPeriod(TemporalAmount period) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime currentStart = now.minus(period);
        LocalDateTime previousStart = currentStart.minus(period);
        LocalDateTime previousEnd = currentStart;

        // Current period
        OrderStatusStatsDTO current = statisticsRepository.getStatsNewCustomerBetween(currentStart, now);
        OrderStatusStatsDTO previous = statisticsRepository.getStatsNewCustomerBetween(previousStart, previousEnd);

        logger.info("current: {}", current.getTotalOrder());
        logger.info("previous: {}", previous.getTotalOrder());

        return new GrowthResult<OrderStatusStatsDTO>(
                current,
                previous,
                calculateGrowth(BigDecimal.valueOf(previous.getTotalOrder()), BigDecimal.valueOf(current.getTotalOrder()))
        );
    }

    @Override
    public GrowthResult<OrderStatusStatsDTO> getNewCustomerStatsByRangeType(String rangeType) {
        switch (rangeType.toLowerCase()) {
            case "day" -> {
                return getStatsNewCustomerByPeriod(Duration.ofDays(1));
            }
            case "week" -> {
                return getStatsNewCustomerByPeriod(Duration.ofDays(7));
            }
            case "month" -> {
                return getStatsNewCustomerByPeriod(Period.ofMonths(1));
            }
            case "quarter" -> {
                return getStatsNewCustomerByPeriod(Period.ofMonths(3));
            }
            case "year" -> {
                return getStatsNewCustomerByPeriod(Period.ofYears(1));
            }
            default ->
                throw new IllegalArgumentException(String.format("Invalid Range: %s", rangeType));
        }
    }

    private PaginationResponse<OrderMnResource> getStatsOrderByRangeType(PaginationRequest request, TemporalAmount period) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime currentStart = now.minus(period);

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), Sort.by("createdAt").descending());

        Page<OrderMnResource> pages = statisticsRepository.findStatsOrder(pageable, currentStart, now);

        return PaginationResponse.<OrderMnResource>builder()
                .content(pages.getContent())
                .pageNumber(pages.getNumber())
                .pageSize(pages.getSize())
                .totalPages(pages.getTotalPages())
                .totalElements(pages.getTotalElements())
                .build();
    }

    @Override
    public PaginationResponse<OrderMnResource> getPagiOrderByRangeType(PaginationRequest request, String rangeType) {
        switch (rangeType.toLowerCase()) {
            case "day" -> {
                return getStatsOrderByRangeType(request, Duration.ofDays(1));
            }
            case "week" -> {
                return getStatsOrderByRangeType(request, Duration.ofDays(7));
            }
            case "month" -> {
                return getStatsOrderByRangeType(request, Period.ofMonths(1));
            }
            case "quarter" -> {
                return getStatsOrderByRangeType(request, Period.ofMonths(3));
            }
            case "year" -> {
                return getStatsOrderByRangeType(request, Period.ofYears(1));
            }
            default ->
                throw new IllegalArgumentException(String.format("Invalid Range: %s", rangeType));
        }
    }

    private Map<UUID, BigDecimal> getStatsTopProduct(Integer limit, TemporalAmount period) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime currentStart = now.minus(period);

        Pageable pageable = PageRequest.of(0, limit);

        List<TopProductDTO> topProductList = statisticsRepository.findTopProductByRangeType(pageable, currentStart, now);

        Map<UUID, BigDecimal> result = topProductList.stream()
                .collect(Collectors.toMap(TopProductDTO::getProductId, TopProductDTO::getTotalRevenue));

        return result;
    }

    @Override
    public Map<UUID, BigDecimal> getTopProductByRangeType(Integer limit, String rangeType) {
        switch (rangeType.toLowerCase()) {
            case "day" -> {
                return getStatsTopProduct(limit, Duration.ofDays(1));
            }
            case "week" -> {
                return getStatsTopProduct(limit, Duration.ofDays(7));
            }
            case "month" -> {
                return getStatsTopProduct(limit, Period.ofMonths(1));
            }
            case "quarter" -> {
                return getStatsTopProduct(limit, Period.ofMonths(3));
            }
            case "year" -> {
                return getStatsTopProduct(limit, Period.ofYears(1));
            }
            default ->
                throw new IllegalArgumentException(String.format("Invalid Range: %s", rangeType));
        }
    }
}
