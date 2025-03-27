package com.service.order.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.order.requests.OrderRequest;
import com.service.order.responses.SuccessResponse;
import com.service.order.services.interfaces.OrderInterface;

@RestController
@RequestMapping("/v1/order")
public class OrderController {

    @Autowired
    private OrderInterface orderInterface;

    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest request) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        orderInterface.createOrder(request)
                )
        );
    }

    @PatchMapping("/update")
    public ResponseEntity<?> updateOrder(@RequestBody OrderRequest request) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        orderInterface.updateOrder(request))
        );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable String id) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        orderInterface.deleteOrder(UUID.fromString(id)))
        );
    }
}
