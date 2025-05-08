package com.service.order.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.service.events.dtos.InvenItemCheckStock;
import com.service.order.requests.ProdSendEmailRequest;

@FeignClient(name = "api-composition")
public interface ApiClient {

    @PostMapping("/inventory-client/inven-item/check-stock")
    ResponseEntity<?> checkInventoryItem(@RequestBody List<InvenItemCheckStock> checkStockList);

    @GetMapping("/order-client/order-item/detail")
    ResponseEntity<?> getOrderItemProd(@RequestParam UUID orderId);

    @PostMapping("/product-client/prod-send-email")
    ResponseEntity<?> getProdSendEmail(@RequestBody List<ProdSendEmailRequest> request);
}
