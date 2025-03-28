package com.service.inventory.wrappers;

import java.util.List;

import com.service.inventory.requests.InventoryItemRequest;

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
public class InventoryItemWrapper {

    private List<InventoryItemRequest> inventoryItemRequests;
}
