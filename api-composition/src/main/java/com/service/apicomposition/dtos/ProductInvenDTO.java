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
public class ProductInvenDTO {

    private String name;
    private String categoryName;
    private String productImageUrl;
}
