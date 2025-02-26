package com.service.apicomposition.wrappers;

import com.service.apicomposition.resources.DiscountClientResource;
import com.service.apicomposition.resources.ProductClientResource;

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
public class ProductDiscountContext {
  private ProductClientResource productClientResource;
  private DiscountClientResource discountClientResource;
}
