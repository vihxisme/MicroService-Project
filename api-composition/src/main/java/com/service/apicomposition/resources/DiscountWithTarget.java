package com.service.apicomposition.resources;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Builder
public class DiscountWithTarget {
  private final Integer idTarget;
  private final UUID discountId;
  private final String discountCode;
  private final String discountTitle;
  private final BigDecimal discountPercentage;
  private final BigDecimal discountAmount;
  private final BigDecimal minOrderValue;
  private final String targetType;
  private final UUID targetId;
}
