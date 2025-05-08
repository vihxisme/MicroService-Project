package com.service.inventory.services.interfaces;

import java.util.Map;
import java.util.UUID;

import com.service.inventory.entities.Inventory;
import com.service.inventory.requests.InventoryRequest;
import com.service.inventory.requests.PaginationRequest;
import com.service.inventory.resources.InventoryProductResource;
import com.service.inventory.resources.InventoryResource;
import com.service.inventory.responses.PaginationResponse;

public interface InventoryInterface {

    Inventory createInventory(InventoryRequest request);

    Inventory updateInventory(InventoryRequest request);

    Boolean deleteInventory(UUID inventoryId);

    PaginationResponse<InventoryResource> getPaginationInventory(PaginationRequest request);

    PaginationResponse<InventoryProductResource> getInventoryProduct(PaginationRequest request);
}
