package com.service.product.resources;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Builder
public class ProductResource {
  private final UUID id;
  private final String productCode;
  private final UUID categorieId;
  private final String name;
  private final BigDecimal price;
  private final String brand;
  private final String status;
  private final String productImageUrl;
}
