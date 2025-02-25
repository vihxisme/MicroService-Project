package com.service.product.requests;

import java.math.BigDecimal;
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
public class ProductRequest {
  private UUID id;
  private String productCode;
  private UUID categorieId;
  private String name;
  private BigDecimal price;
  private String brand;
  private String status;
  private String productImageUrl;
}
