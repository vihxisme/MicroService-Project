package com.service.inventory.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.service.inventory.dtos.InvenItemCheckStock;
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

    @GetMapping("/stock-movement/type-in/by")
    public ResponseEntity<?> getPaginationMovementTypeIN(@RequestParam UUID inventoryId, @ModelAttribute PaginationRequest request) {
        return ResponseEntity.ok(stockMovementInterface.getPaginationMovementTypeIN(request, inventoryId));
    }

    @GetMapping("/stock-movement/type-out/by")
    public ResponseEntity<?> getPaginationMovementTypeOUT(@RequestParam UUID inventoryId, @ModelAttribute PaginationRequest request) {
        return ResponseEntity.ok(stockMovementInterface.getPaginationMovementTypeOUT(request, inventoryId));
    }

    @GetMapping("/stock-movement/type-all")
    public ResponseEntity<?> getPaginationMovementType(@RequestParam UUID inventoryId, @ModelAttribute PaginationRequest request) {
        return ResponseEntity.ok(stockMovementInterface.getPaginationMvmType(request, inventoryId));
    }

    @GetMapping("/stock-movement/type")
    public ResponseEntity<?> getPaginationMovementType(@RequestParam UUID inventoryId, @ModelAttribute PaginationRequest request, @RequestParam String type) {
        return ResponseEntity.ok(stockMovementInterface.getPaginationMvmType(request, inventoryId, type));
    }

    @PostMapping("/inven-item/check-stock")
    public ResponseEntity<?> checkInventoryItem(@RequestBody List<InvenItemCheckStock> checkStockList) {
        return ResponseEntity.ok(inventoryItemInterface.onInventoryItemCheckListener(checkStockList));
    }

}
