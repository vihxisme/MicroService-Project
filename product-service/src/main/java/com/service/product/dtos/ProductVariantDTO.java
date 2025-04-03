package com.service.product.dtos;

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
public class ProductVariantDTO {

    private Integer id;
    private Integer colorId;
    private String colorName;
    private Integer sizeId;
    private String sizeName;
    private Integer stock;
    private String colorImageUrl;
}
