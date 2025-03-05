package com.service.product.resources;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Builder
public class ProdAndStatusResource {

    private final UUID productId;
    private final String productName;
    private final String status;
}
