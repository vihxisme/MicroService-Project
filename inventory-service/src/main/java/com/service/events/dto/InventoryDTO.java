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
public class InventoryDTO {

    private UUID id;
    private String productCode;
    private UUID productId;

    @Builder.Default
    private Integer quantity = 0;

    @Builder.Default
    private Boolean isAllowed = true;
}
