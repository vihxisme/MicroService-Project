package com.service.discount.resources;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Builder
public class DiscountResource {
  private final UUID id;
  private final String discountTitle;
  private final BigDecimal discountPercentage;
  private final BigDecimal discountAmount;
  private final BigDecimal minOrderValue;
  private final String imageUrl;
  private final Timestamp startDate;
  private final Timestamp endDate;
  private final Boolean isActive;
}
