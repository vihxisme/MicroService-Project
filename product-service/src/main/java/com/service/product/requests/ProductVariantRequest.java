package com.service.product.requests;

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
public class ProductVariantRequest {

    private Integer id;
    private UUID productId;
    private Integer colorId;
    private Integer sizeId;
    private String colorImageUrl;

    @Builder.Default
    private Integer stock = 0;
}
