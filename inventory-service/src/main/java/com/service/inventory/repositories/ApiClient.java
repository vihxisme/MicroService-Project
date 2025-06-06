package com.service.inventory.repositories;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "api-composition")
public interface ApiClient {

    @GetMapping("/inventory-client/inventory/with-product")
    ResponseEntity<?> getInventoryProduct(@RequestParam int page, @RequestParam int size);

    @GetMapping("/inventory-client/inventory-items")
    ResponseEntity<?> getItemProductVariant(@RequestParam String inventoryId, @RequestParam int page, @RequestParam int size);

    @GetMapping("/inventory-client/stock-movement/type-in")
    ResponseEntity<?> getStockMovementTypeIN_Product(@RequestParam int page, @RequestParam int size);

    @GetMapping("/inventory-client/stock-movement/type-out")
    ResponseEntity<?> getStockMovementTypeOUT_Product(@RequestParam int page, @RequestParam int size);

    @GetMapping("/inventory-client/stock-movement/type-in/by")
    ResponseEntity<?> getStockMovementTypeINbyInventoryId_Product(@RequestParam UUID inventoryId, @RequestParam int page, @RequestParam int size);

    @GetMapping("/inventory-client/stock-movement/type-out/by")
    ResponseEntity<?> getStockMovementTypeOUTbyInventoryId_Product(@RequestParam UUID inventoryId, @RequestParam int page, @RequestParam int size);

    @GetMapping("/inventory-client/stock-movement/type-all")
    ResponseEntity<?> getStockMovementTypebyInventoryId_Product(@RequestParam UUID inventoryId, @RequestParam int page, @RequestParam int size);

    @GetMapping("/inventory-client/stock-movement/type")
    ResponseEntity<?> getStockMovementTypebyInventoryId_Product(@RequestParam UUID inventoryId, @RequestParam int page, @RequestParam int size, @RequestParam String type);
}
