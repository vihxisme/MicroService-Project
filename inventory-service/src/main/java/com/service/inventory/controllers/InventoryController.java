package com.service.inventory.controllers;

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
import org.springframework.web.bind.annotation.RestController;

import com.service.inventory.requests.InventoryRequest;
import com.service.inventory.requests.PaginationRequest;
import com.service.inventory.responses.SuccessResponse;
import com.service.inventory.services.interfaces.InventoryInterface;

@RestController
@RequestMapping("/v1/inventory")
public class InventoryController {

    @Autowired
    private InventoryInterface inventoryInterface;

    @PostMapping("/create")
    public ResponseEntity<?> createInventory(@RequestBody InventoryRequest request) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        inventoryInterface.createInventory(request))
        );
    }

    @PatchMapping("/update")
    public ResponseEntity<?> updateInventory(@RequestBody InventoryRequest request) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        inventoryInterface.updateInventory(request))
        );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteInventory(@PathVariable String id) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        inventoryInterface.deleteInventory(UUID.fromString(id)))
        );
    }

    @GetMapping("/with-product")
    public ResponseEntity<?> getInventoryProduct(@ModelAttribute PaginationRequest request) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        inventoryInterface.getInventoryProduct(request))
        );
    }

}
