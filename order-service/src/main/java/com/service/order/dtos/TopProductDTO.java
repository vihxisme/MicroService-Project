package com.service.order.dtos;

import java.math.BigDecimal;
import java.util.UUID;

// @NoArgsConstructor
// @AllArgsConstructor
// @Getter
// @Setter
// @Builder
public interface TopProductDTO {

    UUID getProductId();

    BigDecimal getTotalRevenue();
}
