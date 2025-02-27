package com.service.apicomposition.resources;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Builder
public class DiscountClientResource {
  private final UUID id;
  private final BigDecimal discountPercentage;
  private final BigDecimal discountAmount;
  private final BigDecimal minOrderValue;
  private final String targetType;
  private final UUID targetId;
}
