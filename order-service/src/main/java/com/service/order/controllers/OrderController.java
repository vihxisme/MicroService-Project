package com.service.order.controllers;

import java.util.UUID;
import java.util.List;

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

import com.service.order.requests.OrderRequest;
import com.service.order.requests.PaginationRequest;
import com.service.order.responses.SuccessResponse;
import com.service.order.services.interfaces.OrderInterface;
import com.service.order.wrappers.OrderWrapper;

@RestController
@RequestMapping("/v1/order")
public class OrderController {

    @Autowired
    private OrderInterface orderInterface;

    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestBody OrderWrapper wrapper) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        orderInterface.create(wrapper)
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

    @PostMapping("/submit-order")
    public ResponseEntity<?> submitOrder(@RequestBody OrderWrapper wrapper) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        orderInterface.submitOrder(wrapper))
        );
    }

    @GetMapping("/info/list")
    public ResponseEntity<?> getAllOrder(@ModelAttribute PaginationRequest request) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        orderInterface.getAllOrder(request))
        );
    }

    @GetMapping("/statistics")
    public ResponseEntity<?> getOrderStatistics() {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        orderInterface.getOrderStatistics())
        );
    }

    @GetMapping("/detail")
    public ResponseEntity<?> getOrderById(@RequestParam UUID id) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        orderInterface.getOrderById(id)
                )
        );
    }

    @PostMapping("/test-mq")
    public ResponseEntity<?> testMq(@RequestBody List<OrderRequest> request) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        orderInterface.testRabbitMq(request)
                )
        );
    }

    @GetMapping("/body-send-email")
    public ResponseEntity<?> bodySendEmail() {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        orderInterface.bodySendEmail()
                )
        );
    }
}
