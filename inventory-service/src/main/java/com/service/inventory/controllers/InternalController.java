package com.service.inventory.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.service.inventory.requests.PaginationRequest;
import com.service.inventory.services.interfaces.InventoryInterface;
import com.service.inventory.services.interfaces.InventoryItemInterface;
import com.service.inventory.services.interfaces.StockMovementInterface;

@RestController
@RequestMapping("/internal")
public class InternalController {

    @Autowired
    private InventoryInterface inventoryInterface;

    @Autowired
    private InventoryItemInterface inventoryItemInterface;

    @Autowired
    private StockMovementInterface stockMovementInterface;

    @GetMapping("/inventory")
    public ResponseEntity<?> getPaginationInventory(@ModelAttribute PaginationRequest request) {
        return ResponseEntity.ok(inventoryInterface.getPaginationInventory(request));
    }

    @GetMapping("/inventory-items")
    public ResponseEntity<?> getPaginationInventoryItem(@RequestParam String inventoryId, @ModelAttribute PaginationRequest request) {
        return ResponseEntity.ok(inventoryItemInterface.getPaginationInventoryItem(UUID.fromString(inventoryId), request));
    }

    @GetMapping("/stock-movement/type-in")
    public ResponseEntity<?> getPaginationMovementTypeIN(@ModelAttribute PaginationRequest request) {
        return ResponseEntity.ok(stockMovementInterface.getPaginationMovementTypeIN(request));
    }

    @GetMapping("/stock-movement/type-out")
    public ResponseEntity<?> getPaginationMovementTypeOUT(@ModelAttribute PaginationRequest request) {
        return ResponseEntity.ok(stockMovementInterface.getPaginationMovementTypeOUT(request));
    }

}
