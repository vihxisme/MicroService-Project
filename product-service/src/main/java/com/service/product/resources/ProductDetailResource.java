package com.service.product.resources;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Builder
public class ProductDetailResource {
  private final Integer id;
  private final String attributeName;
  private final String attributeValue;
}
