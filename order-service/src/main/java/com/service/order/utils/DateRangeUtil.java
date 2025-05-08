package com.service.order.utils;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.Year;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAdjusters;

import org.springframework.data.util.Pair;

public class DateRangeUtil {

    /**
     * Lấy phạm vi thời gian của một khoảng thời gian xác định (ngày, tuần,
     * tháng, quý, năm) cho thời gian hiện tại.
     *
     * Phương thức này trả về một cặp giá trị `Instant` đại diện cho thời gian
     * bắt đầu và kết thúc của khoảng thời gian tương ứng với loại `rangeType`.
     * Các loại phạm vi có thể bao gồm: - "day": Phạm vi thời gian cho ngày hôm
     * nay (từ 00:00 đến 23:59). - "week": Phạm vi thời gian cho tuần hiện tại
     * (từ thứ Hai đến Chủ Nhật). - "month": Phạm vi thời gian cho tháng hiện
     * tại (từ ngày 1 đến ngày cuối cùng của tháng). - "quarter": Phạm vi thời
     * gian cho quý hiện tại (ba tháng, từ đầu quý đến cuối quý). - "year": Phạm
     * vi thời gian cho năm hiện tại (từ ngày 1 tháng 1 đến ngày 31 tháng 12).
     *
     * Phương thức sử dụng `ZoneOffset.UTC` để chuyển đổi thời gian về chuẩn
     * UTC.
     *
     * @param rangeType Loại phạm vi thời gian cần lấy (day, week, month,
     * quarter, year).
     * @return Pair<Instant, Instant> Cặp thời gian bắt đầu và kết thúc của phạm
     * vi thời gian.
     *
     * @throws IllegalArgumentException Nếu `rangeType` không hợp lệ.
     */
    /**
     * Retrieves the time range for a specified period type (day, week, month,
     * quarter, year) for the current time.
     *
     * This method returns a pair of `Instant` values representing the start and
     * end times of the specified time period based on the `rangeType` argument.
     * The possible range types are: - "day": Time range for the current day
     * (from 00:00 to 23:59). - "week": Time range for the current week (from
     * Monday to Sunday). - "month": Time range for the current month (from the
     * 1st day to the last day of the month). - "quarter": Time range for the
     * current quarter (three months, from the start of the quarter to the end
     * of the quarter). - "year": Time range for the current year (from January
     * 1st to December 31st).
     *
     * The method uses `ZoneOffset.UTC` to convert the time to UTC.
     *
     * @param rangeType The type of time range to retrieve (day, week, month,
     * quarter, year).
     * @return Pair<Instant, Instant> A pair of start and end times for the
     * specified time range.
     *
     * @throws IllegalArgumentException If the `rangeType` is invalid.
     */
    public static Pair<Instant, Instant> getRange(String rangeType) {
        LocalDateTime now = LocalDateTime.now();
        switch (rangeType.toLowerCase()) {
            case "day" -> {
                LocalDate today = now.toLocalDate();
                return Pair.of(today.atStartOfDay().toInstant(ZoneOffset.UTC),
                        today.atTime(LocalTime.MAX).toInstant(ZoneOffset.UTC));
            }
            case "week" -> {
                LocalDate today = now.toLocalDate();
                LocalDate start = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                LocalDate end = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
                return Pair.of(start.atStartOfDay().toInstant(ZoneOffset.UTC),
                        end.atTime(LocalTime.MAX).toInstant(ZoneOffset.UTC));
            }
            case "month" -> {
                LocalDate start = now.withDayOfMonth(1).toLocalDate();
                LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
                return Pair.of(start.atStartOfDay().toInstant(ZoneOffset.UTC),
                        end.atTime(LocalTime.MAX).toInstant(ZoneOffset.UTC));
            }
            case "quarter" -> {
                int currentQuarter = (now.getMonthValue() - 1) / 3 + 1;
                Month startMonth = Month.of((currentQuarter - 1) * 3 + 1);
                Month endMonth = Month.of(currentQuarter * 3);

                LocalDate start = LocalDate.of(now.getYear(), startMonth, 1);
                LocalDate end = LocalDate.of(now.getYear(), endMonth, endMonth.length(Year.isLeap(now.getYear())));

                return Pair.of(start.atStartOfDay().toInstant(ZoneOffset.UTC),
                        end.atTime(LocalTime.MAX).toInstant(ZoneOffset.UTC));
            }
            case "year" -> {
                LocalDate start = LocalDate.of(now.getYear(), 1, 1);
                LocalDate end = LocalDate.of(now.getYear(), 12, 31);
                return Pair.of(start.atStartOfDay().toInstant(ZoneOffset.UTC),
                        end.atTime(LocalTime.MAX).toInstant(ZoneOffset.UTC));
            }
            default ->
                throw new IllegalArgumentException("Invalid range: " + rangeType);
        }
    }

    /**
     * Lấy phạm vi thời gian của một khoảng thời gian xác định (ngày, tuần,
     * tháng, quý, năm) cho khoảng thời gian trước đó.
     *
     * Phương thức này trả về một cặp giá trị `Instant` đại diện cho thời gian
     * bắt đầu và kết thúc của khoảng thời gian trước đó, tương ứng với loại
     * `rangeType`. Các loại phạm vi có thể bao gồm: - "day": Phạm vi thời gian
     * cho ngày hôm qua (từ 00:00 đến 23:59). - "week": Phạm vi thời gian cho
     * tuần trước (từ thứ Hai đến Chủ Nhật của tuần trước). - "month": Phạm vi
     * thời gian cho tháng trước (từ ngày 1 đến ngày cuối cùng của tháng trước).
     * - "quarter": Phạm vi thời gian cho quý trước (ba tháng, từ đầu quý đến
     * cuối quý trước). - "year": Phạm vi thời gian cho năm trước (từ ngày 1
     * tháng 1 đến ngày 31 tháng 12 của năm trước).
     *
     * Phương thức sử dụng `ZoneOffset.UTC` để chuyển đổi thời gian về chuẩn
     * UTC.
     *
     * @param rangeType Loại phạm vi thời gian cần lấy (day, week, month,
     * quarter, year).
     * @return Pair<Instant, Instant> Cặp thời gian bắt đầu và kết thúc của phạm
     * vi thời gian trước đó.
     *
     * @throws IllegalArgumentException Nếu `rangeType` không hợp lệ.
     */
    /**
     * Retrieves the time range for a specified period type (day, week, month,
     * quarter, year) for the previous period.
     *
     * This method returns a pair of `Instant` values representing the start and
     * end times of the previous period, corresponding to the `rangeType`
     * argument. The possible range types are: - "day": Time range for the
     * previous day (from 00:00 to 23:59). - "week": Time range for the previous
     * week (from Monday to Sunday of the previous week). - "month": Time range
     * for the previous month (from the 1st day to the last day of the previous
     * month). - "quarter": Time range for the previous quarter (three months,
     * from the start of the previous quarter to the end of the previous
     * quarter). - "year": Time range for the previous year (from January 1st to
     * December 31st of the previous year).
     *
     * The method uses `ZoneOffset.UTC` to convert the time to UTC.
     *
     * @param rangeType The type of time range to retrieve (day, week, month,
     * quarter, year).
     * @return Pair<Instant, Instant> A pair of start and end times for the
     * previous time range.
     *
     * @throws IllegalArgumentException If the `rangeType` is invalid.
     */
    /**
     * Retrieves the time range for a specified period type (day, week, month,
     * quarter, year) for the previous period.
     *
     * This method returns a pair of `Instant` values representing the start and
     * end times of the previous period, corresponding to the `rangeType`
     * argument. The possible range types are: - "day": Time range for the
     * previous day (from 00:00 to 23:59). - "week": Time range for the previous
     * week (from Monday to Sunday of the previous week). - "month": Time range
     * for the previous month (from the 1st day to the last day of the previous
     * month). - "quarter": Time range for the previous quarter (three months,
     * from the start of the previous quarter to the end of the previous
     * quarter). - "year": Time range for the previous year (from January 1st to
     * December 31st of the previous year).
     *
     * The method uses `ZoneOffset.UTC` to convert the time to UTC.
     *
     * @param rangeType The type of time range to retrieve (day, week, month,
     * quarter, year).
     * @return Pair<Instant, Instant> A pair of start and end times for the
     * previous time range.
     *
     * @throws IllegalArgumentException If the `rangeType` is invalid.
     */
    public static Pair<Instant, Instant> getPrevRange(String rangeType) {
        LocalDateTime now = LocalDateTime.now();
        switch (rangeType.toLowerCase()) {
            case "day" -> {
                LocalDate yesterday = now.toLocalDate().minusDays(1);
                return Pair.of(yesterday.atStartOfDay().toInstant(ZoneOffset.UTC),
                        yesterday.atTime(LocalTime.MAX).toInstant(ZoneOffset.UTC));
            }
            case "week" -> {
                LocalDate today = now.toLocalDate();
                LocalDate start = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).minusWeeks(1);
                LocalDate end = start.plusDays(6);
                return Pair.of(start.atStartOfDay().toInstant(ZoneOffset.UTC),
                        end.atTime(LocalTime.MAX).toInstant(ZoneOffset.UTC));
            }
            case "month" -> {
                LocalDate firstDayOfThisMonth = now.withDayOfMonth(1).toLocalDate();
                LocalDate start = firstDayOfThisMonth.minusMonths(1);
                LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
                return Pair.of(start.atStartOfDay().toInstant(ZoneOffset.UTC),
                        end.atTime(LocalTime.MAX).toInstant(ZoneOffset.UTC));
            }
            case "quarter" -> {
                int currentQuarter = (now.getMonthValue() - 1) / 3 + 1;
                int prevQuarter = currentQuarter - 1;
                int year = now.getYear();

                if (prevQuarter == 0) {
                    prevQuarter = 4;
                    year -= 1;
                }

                Month startMonth = Month.of((prevQuarter - 1) * 3 + 1);
                Month endMonth = Month.of(prevQuarter * 3);

                LocalDate start = LocalDate.of(year, startMonth, 1);
                LocalDate end = LocalDate.of(year, endMonth, endMonth.length(Year.isLeap(year)));

                return Pair.of(start.atStartOfDay().toInstant(ZoneOffset.UTC),
                        end.atTime(LocalTime.MAX).toInstant(ZoneOffset.UTC));
            }
            case "year" -> {
                int year = now.getYear() - 1;
                LocalDate start = LocalDate.of(year, 1, 1);
                LocalDate end = LocalDate.of(year, 12, 31);
                return Pair.of(start.atStartOfDay().toInstant(ZoneOffset.UTC),
                        end.atTime(LocalTime.MAX).toInstant(ZoneOffset.UTC));
            }
            default ->
                throw new IllegalArgumentException("Invalid range: " + rangeType);
        }
    }

    // public static Pair<Instant, Instant> getRange(String rangeType) {
    //     LocalDateTime now = LocalDateTime.now();
    //     switch (rangeType) {
    //         case "day" -> {
    //             LocalDate today = now.toLocalDate();
    //             return Pair.of(today.atStartOfDay().toInstant(ZoneOffset.UTC),
    //                            today.atTime(23, 59, 59).toInstant(ZoneOffset.UTC));
    //         }
    //         case "week" -> {
    //             LocalDate start = now.with(DayOfWeek.MONDAY).toLocalDate();
    //             LocalDate end = now.with(DayOfWeek.SUNDAY).toLocalDate();
    //             return Pair.of(start.atStartOfDay().toInstant(ZoneOffset.UTC),
    //                            end.atTime(23, 59, 59).toInstant(ZoneOffset.UTC));
    //         }
    //         case "month" -> {
    //             LocalDate start = now.withDayOfMonth(1).toLocalDate();
    //             LocalDate end = now.withDayOfMonth(now.toLocalDate().lengthOfMonth()).toLocalDate();
    //             return Pair.of(start.atStartOfDay().toInstant(ZoneOffset.UTC),
    //                            end.atTime(23, 59, 59).toInstant(ZoneOffset.UTC));
    //         }
    //         case "quarter" -> {
    //             int currentQuarter = (now.getMonthValue() - 1) / 3 + 1;
    //             Month startMonth = Month.of((currentQuarter - 1) * 3 + 1);
    //             Month endMonth = Month.of(currentQuarter * 3);
    //             LocalDate start = LocalDate.of(now.getYear(), startMonth, 1);
    //             LocalDate end = LocalDate.of(now.getYear(), endMonth, endMonth.length(Year.isLeap(now.getYear())));
    //             return Pair.of(start.atStartOfDay().toInstant(ZoneOffset.UTC),
    //                            end.atTime(23, 59, 59).toInstant(ZoneOffset.UTC));
    //         }
    //         case "year" -> {
    //             LocalDate start = LocalDate.of(now.getYear(), 1, 1);
    //             LocalDate end = LocalDate.of(now.getYear(), 12, 31);
    //             return Pair.of(start.atStartOfDay().toInstant(ZoneOffset.UTC),
    //                            end.atTime(23, 59, 59).toInstant(ZoneOffset.UTC));
    //         }
    //         default -> throw new IllegalArgumentException("Invalid range: " + rangeType);
    //     }
    // }
}
