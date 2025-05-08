package com.service.order.dtos;

import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class StatTimeRange {

    private String label;              // "Hôm nay", "Tháng này", v.v.
    private ZonedDateTime start;       // thời gian bắt đầu kỳ hiện tại
    private ZonedDateTime end;       // thời gian kết thúc kỳ hiện tại
    private ZonedDateTime prevStart;   // thời gian bắt đầu kỳ trước
    private ZonedDateTime prevEnd;      // thời gian kết thúc kỳ trước
}
