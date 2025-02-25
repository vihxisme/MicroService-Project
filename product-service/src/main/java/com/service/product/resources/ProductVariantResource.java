package com.service.product.resources;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Builder
public class ProductVariantResource {
  private final Integer id;
  private final UUID productId;
  private final Integer colorId;
  private final Integer sizeId;
  private final BigDecimal price;
  private final int stock;
  private final String colorImageUrl;
}
