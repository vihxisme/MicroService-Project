package com.service.order.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.order.requests.OrderItemRequest;
import com.service.order.responses.SuccessResponse;
import com.service.order.services.interfaces.OrderItemInterface;

@RestController
@RequestMapping("/v1/order-item")
public class OrderItemController {

    @Autowired
    private OrderItemInterface orderItemInterface;

    @PostMapping("/create")
    public ResponseEntity<?> createOrderItems(@RequestBody List<OrderItemRequest> requests) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        orderItemInterface.createOrderItems(requests))
        );
    }

    @PatchMapping("/update")
    public ResponseEntity<?> updateOrderItems(@RequestBody List<OrderItemRequest> requests) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        orderItemInterface.updateOrderItems(requests))
        );
    }

    @DeleteMapping("/delete/{ids}")
    public ResponseEntity<?> deleteOrderItems(@PathVariable List<Integer> ids) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        orderItemInterface.deleteOrderItems(ids))
        );
    }
}
