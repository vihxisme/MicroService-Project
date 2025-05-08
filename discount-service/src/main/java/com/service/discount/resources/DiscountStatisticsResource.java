package com.service.discount.resources;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Builder
public class DiscountStatisticsResource {

    private final Long totalDiscounts;
    private final Long activeDiscounts;
    private final Long discountedProducts;
    private final Long discountedCategories;
}
