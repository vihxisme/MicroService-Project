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
public class DiscountWithTargetNameResource {
  private final Integer idTarget;
  private final UUID discountId;
  private final String discountCode;
  private final String discountTitle;
  private final BigDecimal discountPercentage;
  private final BigDecimal discountAmount;
  private final BigDecimal minOrderValue;
  private final UUID targetId;
  private final String targetName;
}
