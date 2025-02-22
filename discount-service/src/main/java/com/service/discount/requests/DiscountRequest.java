package com.service.discount.requests;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

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
public class DiscountRequest {
  private UUID id;
  private String discountCode;

  private Long discountType;

  private String discountTitle;
  private BigDecimal discountPercentage;
  private BigDecimal discountAmount;
  private BigDecimal minOrderValue;
  private String imageUrl;
  private Timestamp startDate;
  private Timestamp endDate;

  @Builder.Default
  private Boolean isActive = true;
}
