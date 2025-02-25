package com.service.product.wrapper;

import com.service.product.requests.ProductVariantRequest;
import com.service.product.requests.VariantRequest;

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
public class ProductVariantWrapper {
  private ProductVariantRequest productVariantRequest;
  private VariantRequest variantRequest;
}
