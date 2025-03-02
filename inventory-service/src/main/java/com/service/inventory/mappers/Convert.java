package com.service.inventory.mappers;

import java.util.UUID;

import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.service.inventory.entities.Inventory;
import com.service.inventory.entities.InventoryItem;
import com.service.inventory.entities.StockMovement;
import com.service.inventory.enums.MovementEnum;
import com.service.inventory.repositories.InventoryItemRepository;
import com.service.inventory.repositories.InventoryRepository;

import jakarta.persistence.EntityNotFoundException;

@Component
public class Convert {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private InventoryItemRepository inventoryItemRepository;

    @Named("inventoryFromId")
    public Inventory inventoryFromId(UUID inventoryId) {
        return inventoryRepository.findById(inventoryId).orElseThrow(()
                -> new EntityNotFoundException("Inventory not found"));
    }

    @Named("stringToMovementEnum")
    public MovementEnum stringToMovementEnum(String value) {
        return MovementEnum.valueOf(value);
    }

    @Named("idToInventoryItem")
    public InventoryItem idToInventoryItem(UUID inventoryItemId) {
        return inventoryItemRepository.findById(inventoryItemId).orElseThrow(()
                -> new EntityNotFoundException("InventoryItem not found"));
    }

    @Named("inventoryItemToUUID")
    public UUID inventoryItemToUUID(InventoryItem inventoryItem) {
        return inventoryItem.getInventory().getId();
    }

    @Named("stockMovementToInventoryItem")
    public UUID stockMovementToInventoryItem(StockMovement stockMovement) {
        return stockMovement.getInventoryItem().getId();
    }

    @Named("movementToString")
    public String movementToString(MovementEnum movementEnum) {
        return movementEnum != null ? movementEnum.toString() : null;
    }
}
