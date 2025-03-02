package com.service.inventory.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.service.inventory.entities.StockMovement;
import com.service.inventory.requests.StockMovementRequest;
import com.service.inventory.resources.StockMovementResource;

@Mapper(componentModel = "spring", uses = {Convert.class}, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface StockMovementMapper {

    @Mapping(target = "inventoryItem", source = "inventoryItemId", qualifiedByName = "idToInventoryItem")
    @Mapping(target = "movementStatus", source = "movementStatus", qualifiedByName = "stringToMovementEnum")
    @Mapping(target = "movementType", source = "movementType", qualifiedByName = "stringToMovementEnum")
    StockMovement toStockMovement(StockMovementRequest request);

    @Mapping(target = "inventoryItem", source = "inventoryItemId", qualifiedByName = "idToInventoryItem")
    @Mapping(target = "movementStatus", source = "movementStatus", qualifiedByName = "stringToMovementEnum")
    @Mapping(target = "movementType", source = "movementType", qualifiedByName = "stringToMovementEnum")
    void updateStockMovementFromRequest(StockMovementRequest request, @MappingTarget StockMovement stockMovement);

    @Mapping(target = "movementType", source = "movementType", qualifiedByName = "movementToString")
    @Mapping(target = "movementStatus", source = "movementStatus", qualifiedByName = "movementToString")
    StockMovementResource toStockMovementResource(StockMovement stockMovement);
}
