package com.service.order.dtos;

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
public class WriteEmailDTO {

    private UUID productId;
    private Integer variantId;
    private String name;
    private BigDecimal price;
    private String imageUrl;
    private String colorName;
    private String sizeName;
    private Integer quantity;
}
