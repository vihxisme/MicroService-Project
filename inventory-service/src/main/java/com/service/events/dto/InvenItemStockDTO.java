package com.service.events.dto;

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
public class InvenItemStockDTO {

    private UUID orderId;
    private String orderCode;
    private Integer prodVariantId;
    private Integer itemQuantity;
}
