package com.service.inventory.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.service.inventory.requests.PaginationRequest;
import com.service.inventory.requests.StockMovementRequest;
import com.service.inventory.responses.SuccessResponse;
import com.service.inventory.services.interfaces.StockMovementInterface;

@RestController
@RequestMapping("/v1/stock-movement")
public class StockMovementController {

    @Autowired
    private StockMovementInterface stockMovementInterface;

    @PostMapping("/create")
    public ResponseEntity<?> createStockMovement(@RequestBody List<StockMovementRequest> requests) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        stockMovementInterface.createStockMovements(requests))
        );
    }

    @PatchMapping("/update")
    public ResponseEntity<?> createStockMovement(@RequestBody StockMovementRequest request) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        stockMovementInterface.updateStockMovement(request))
        );
    }

    @GetMapping("/type-in/with-prod")
    public ResponseEntity<?> getStockMovementTypeIN_Product(@ModelAttribute PaginationRequest request) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        stockMovementInterface.getStockMovementTypeIN_Product(request))
        );
    }

    @GetMapping("/type-out/with-prod")
    public ResponseEntity<?> getStockMovementTypeOUT_Product(@ModelAttribute PaginationRequest request) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        stockMovementInterface.getStockMovementTypeOUT_Product(request))
        );
    }

    @GetMapping("/type-in/with-prod/by")
    public ResponseEntity<?> getStockMovementTypeIN_Product(@RequestParam UUID inventoryId, @ModelAttribute PaginationRequest request) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        stockMovementInterface.getStockMovementTypeIN_Product(request, inventoryId))
        );
    }

    @GetMapping("/type-out/with-prod/by")
    public ResponseEntity<?> getStockMovementTypeOUT_Product(@RequestParam UUID inventoryId, @ModelAttribute PaginationRequest request) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        stockMovementInterface.getStockMovementTypeOUT_Product(request, inventoryId))
        );
    }

    @GetMapping("/type-all")
    public ResponseEntity<?> getStockMovementType_Product(@RequestParam UUID inventoryId, @ModelAttribute PaginationRequest request) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        stockMovementInterface.getStockMvmType_Product(request, inventoryId))
        );
    }

    @GetMapping("/type")
    public ResponseEntity<?> getStockMovementType_Product(@RequestParam UUID inventoryId, @ModelAttribute PaginationRequest request, @RequestParam String type) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        stockMovementInterface.getStockMvmType_Product(request, inventoryId, type))
        );
    }
}
