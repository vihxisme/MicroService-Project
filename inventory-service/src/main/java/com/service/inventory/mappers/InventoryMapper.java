package com.service.inventory.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.service.inventory.entities.Inventory;
import com.service.inventory.requests.InventoryRequest;
import com.service.inventory.resources.InventoryResource;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface InventoryMapper {

    Inventory toInventory(InventoryRequest request);

    void updateInventoryFromRequest(InventoryRequest request, @MappingTarget Inventory inventory);

    InventoryResource toInventoryResource(Inventory inventory);
}
