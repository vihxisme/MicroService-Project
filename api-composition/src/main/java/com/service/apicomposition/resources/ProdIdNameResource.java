package com.service.apicomposition.resources;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Builder
public class ProdIdNameResource {

    private final String productCode;
    private final String productName;
}
