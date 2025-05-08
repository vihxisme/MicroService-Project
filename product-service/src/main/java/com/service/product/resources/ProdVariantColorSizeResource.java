package com.service.product.resources;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Builder
public class ProdVariantColorSizeResource {

    private final String colorName;
    private final String sizeName;
    private final String imageVariant;
}
