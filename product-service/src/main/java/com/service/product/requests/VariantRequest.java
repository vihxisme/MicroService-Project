package com.service.product.requests;

import java.util.List;
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
public class VariantRequest {

    private UUID productId;
    private List<Integer> colorIds;
    private List<Integer> sizeIds;
    private List<String> colorImageUrls;
}
