package com.service.customer.dtos;

import java.math.BigDecimal;

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
public class GrowthResult<T> {

    T current;
    T previous;
    BigDecimal growthPercent;
    // BigDecimal growthOrderPercent;
}
