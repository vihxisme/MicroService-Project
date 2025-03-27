package com.service.inventory.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.service.inventory.requests.InventoryItemRequest;
import com.service.inventory.requests.PaginationRequest;
import com.service.inventory.responses.SuccessResponse;
import com.service.inventory.services.interfaces.InventoryItemInterface;

@RestController
@RequestMapping("/v1/inventory-items")
public class InventoryItemController {

    @Autowired
    private InventoryItemInterface inventoryItemInterface;

    @PostMapping("/create")
    public ResponseEntity<?> createInventoryItem(@RequestBody List<InventoryItemRequest> requests) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        inventoryItemInterface.createInventoryItem(requests))
        );
    }

    @PatchMapping("/update")
    public ResponseEntity<?> updateInventoryItem(@RequestBody InventoryItemRequest request) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        inventoryItemInterface.updateInventoryItem(request))
        );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteInventoryItem(@PathVariable String id) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        inventoryItemInterface.deleteInventoryItem(UUID.fromString(id)))
        );
    }

    @PostMapping("/inventory-intake")
    public ResponseEntity<?> inventoryIntake(@RequestBody InventoryItemRequest request) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        inventoryItemInterface.inventoryIntake(request.getId(), request.getItemQuantity())
                )
        );
    }

    @GetMapping("/with-variant")
    public ResponseEntity<?> getItemProdVariant(@RequestParam String inventoryId, @ModelAttribute PaginationRequest request) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        inventoryItemInterface.getPaginationItemProdVariant(inventoryId, request))
        );
    }
}
