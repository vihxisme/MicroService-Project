package com.service.apicomposition.dtos;

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
    private String colorName;
    private String sizeName;
    private Integer stock;
    private String colorImageUrl;
}
