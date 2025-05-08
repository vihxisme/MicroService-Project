package com.service.inventory.services.interfaces;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.service.inventory.dtos.InvenItemCheckStock;
import com.service.inventory.entities.InventoryItem;
import com.service.inventory.requests.InventoryItemRequest;
import com.service.inventory.requests.PaginationRequest;
import com.service.inventory.resources.ItemProdVariantResource;
import com.service.inventory.resources.ItemResource;
import com.service.inventory.responses.PaginationResponse;

public interface InventoryItemInterface {

    List<InventoryItem> createInventoryItem(List<InventoryItemRequest> requests);

    InventoryItem updateInventoryItem(InventoryItemRequest request);

    Boolean deleteInventoryItem(UUID id);

    InventoryItem inventoryIntake(UUID id, int quantity);

    PaginationResponse<ItemResource> getPaginationInventoryItem(UUID inventoryId, PaginationRequest request);

    PaginationResponse<ItemProdVariantResource> getPaginationItemProdVariant(String inventoryId, PaginationRequest request);

    Map<Integer, Boolean> onInventoryItemCheckListener(List<InvenItemCheckStock> checkStock);
}
