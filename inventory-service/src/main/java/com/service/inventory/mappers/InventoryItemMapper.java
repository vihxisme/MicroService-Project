package com.service.inventory.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.service.inventory.entities.InventoryItem;
import com.service.inventory.requests.InventoryItemRequest;
import com.service.inventory.resources.ItemResource;

@Mapper(componentModel = "spring", uses = {Convert.class}, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface InventoryItemMapper {

    @Mapping(target = "inventory", source = "inventoryId", qualifiedByName = "inventoryFromId")
    InventoryItem toInInventoryItem(InventoryItemRequest request);

    @Mapping(target = "inventory", source = "inventoryId", qualifiedByName = "inventoryFromId")
    void updateInventoryItemFromRequest(InventoryItemRequest request, @MappingTarget InventoryItem inventoryItem);

    @Mapping(target = "inventoryId", source = "inventoryItem", qualifiedByName = "inventoryItemToUUID")
    ItemResource toItemResource(InventoryItem inventoryItem);
}
