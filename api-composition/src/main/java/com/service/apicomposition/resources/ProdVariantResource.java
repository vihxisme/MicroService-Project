package com.service.apicomposition.resources;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Builder
public class ProdVariantResource {

    private final int id;
    private final int colorId;
    private final String colorName;
    private final int sizeId;
    private final String sizeName;
}
