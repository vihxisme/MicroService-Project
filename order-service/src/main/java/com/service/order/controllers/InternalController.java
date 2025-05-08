package com.service.order.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.service.order.services.interfaces.OrderInterface;
import com.service.order.services.interfaces.OrderItemInterface;
import com.service.order.services.interfaces.StatisticsInterface;

@RestController
@RequestMapping("/internal")
public class InternalController {

    @Autowired
    private OrderInterface orderInterface;

    @Autowired
    private OrderItemInterface orderItemInterface;

    @Autowired
    private StatisticsInterface statisticsInterface;

    @GetMapping("/caculate-transaction")
    public ResponseEntity<?> caculateTransaction(@RequestParam List<UUID> userIds) {
        return ResponseEntity.ok(orderInterface.caculateTransaction(userIds));
    }

    @GetMapping("/order-item/by")
    public ResponseEntity<?> getOrderItem(@RequestParam UUID orderId) {
        return ResponseEntity.ok(orderItemInterface.getOrderITemByOrderId(orderId));
    }

    @GetMapping("/stats/top-product/by-revenue")
    public ResponseEntity<?> getTopProductByRevenue(@RequestParam Integer limit) {
        return ResponseEntity.ok(orderInterface.getTopProductByRevenue(limit));
    }

    @GetMapping("/stats/top-product/by")
    public ResponseEntity<?> getTopProductByRangeType(@RequestParam Integer limit, @RequestParam String rangeType) {
        return ResponseEntity.ok(statisticsInterface.getTopProductByRangeType(limit, rangeType));
    }
}
