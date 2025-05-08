package com.service.order.utils;

import java.time.DayOfWeek;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import com.service.order.dtos.StatTimeRange;

public class TimeRangeUtil {

    /**
     * Xây dựng danh sách các khoảng thời gian cần thiết để thống kê doanh thu,
     * bao gồm: - Hôm nay - Tuần này - Tháng này - Quý này - Năm nay
     *
     * Mỗi khoảng thời gian bao gồm: - start: Thời gian bắt đầu của khoảng thời
     * gian. - end: Thời gian kết thúc của khoảng thời gian. - prevStart: Thời
     * gian bắt đầu của khoảng thời gian trước đó. - prevEnd: Thời gian kết thúc
     * của khoảng thời gian trước đó.
     *
     * Các khoảng thời gian này sẽ được sử dụng để tính toán doanh thu hiện tại
     * và doanh thu của khoảng thời gian trước đó trong bảng điều khiển.
     *
     * @return List<StatTimeRange> Danh sách các đối tượng StatTimeRange đại
     * diện cho các khoảng thời gian.
     *
     * Builds a list of the required time ranges for revenue statistics,
     * including: - Today - This week - This month - This quarter - This year
     *
     * Each time range includes: - start: The start time of the range. - end:
     * The end time of the range. - prevStart: The start time of the previous
     * period. - prevEnd: The end time of the previous period.
     *
     * These time ranges will be used to calculate the current revenue and
     * revenue from the previous period for the dashboard.
     *
     * @return List<StatTimeRange> A list of StatTimeRange objects representing
     * the time ranges.
     */
    public static List<StatTimeRange> buildTimeRanges() {
        List<StatTimeRange> ranges = new ArrayList<>();
        ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());

        // Hôm nay
        ZonedDateTime todayStart = now.truncatedTo(ChronoUnit.DAYS);
        ZonedDateTime todayEnd = todayStart.plusDays(1).minusNanos(1);
        ZonedDateTime yesterdayStart = todayStart.minusDays(1);
        ZonedDateTime yesterdayEnd = todayStart.minusNanos(1);

        ranges.add(StatTimeRange.builder()
                .label("Hôm nay")
                .start(todayStart)
                .end(todayEnd)
                .prevStart(yesterdayStart)
                .prevEnd(yesterdayEnd)
                .build());

        // Tuần này
        ZonedDateTime weekStart = now.with(DayOfWeek.MONDAY).truncatedTo(ChronoUnit.DAYS);
        ZonedDateTime weekEnd = weekStart.plusWeeks(1).minusNanos(1);
        ZonedDateTime prevWeekStart = weekStart.minusWeeks(1);
        ZonedDateTime prevWeekEnd = weekStart.minusNanos(1);

        ranges.add(StatTimeRange.builder()
                .label("Tuần này")
                .start(weekStart)
                .end(weekEnd)
                .prevStart(prevWeekStart)
                .prevEnd(prevWeekEnd)
                .build());

        // Tháng này
        ZonedDateTime monthStart = now.withDayOfMonth(1).truncatedTo(ChronoUnit.DAYS);
        ZonedDateTime monthEnd = monthStart.plusMonths(1).minusNanos(1);
        ZonedDateTime prevMonthStart = monthStart.minusMonths(1);
        ZonedDateTime prevMonthEnd = monthStart.minusNanos(1);

        ranges.add(StatTimeRange.builder()
                .label("Tháng này")
                .start(monthStart)
                .end(monthEnd)
                .prevStart(prevMonthStart)
                .prevEnd(prevMonthEnd)
                .build());

        // Quý này
        int currentQuarter = (now.getMonthValue() - 1) / 3 + 1;
        int quarterStartMonth = (currentQuarter - 1) * 3 + 1;
        ZonedDateTime quarterStart = now.withMonth(quarterStartMonth).withDayOfMonth(1).truncatedTo(ChronoUnit.DAYS);
        ZonedDateTime quarterEnd = quarterStart.plusMonths(3).minusNanos(1);
        ZonedDateTime prevQuarterStart = quarterStart.minusMonths(3);
        ZonedDateTime prevQuarterEnd = quarterStart.minusNanos(1);

        ranges.add(StatTimeRange.builder()
                .label("Quý này")
                .start(quarterStart)
                .end(quarterEnd)
                .prevStart(prevQuarterStart)
                .prevEnd(prevQuarterEnd)
                .build());

        // Năm nay
        ZonedDateTime yearStart = now.withDayOfYear(1).truncatedTo(ChronoUnit.DAYS);
        ZonedDateTime yearEnd = yearStart.plusYears(1).minusNanos(1);
        ZonedDateTime prevYearStart = yearStart.minusYears(1);
        ZonedDateTime prevYearEnd = yearStart.minusNanos(1);

        ranges.add(StatTimeRange.builder()
                .label("Năm nay")
                .start(yearStart)
                .end(yearEnd)
                .prevStart(prevYearStart)
                .prevEnd(prevYearEnd)
                .build());

        return ranges;
    }
}
