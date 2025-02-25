package com.service.product.requests;

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
public class VariantRequest {
  private Integer[] colorIds;
  private Integer[] sizeIds;
  private String[] colorImageUrls;
}
