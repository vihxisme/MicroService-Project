package com.service.order.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.service.order.requests.PaginationRequest;
import com.service.order.responses.SuccessResponse;
import com.service.order.services.interfaces.StatisticsInterface;

@RestController
@RequestMapping("/v1/statistics")
public class StatisticsControler {

    @Autowired
    private StatisticsInterface statisticsInterface;

    @GetMapping("/anal-revenue")
    public ResponseEntity<?> getDashboardStats() {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        statisticsInterface.getDashboardStats())
        );
    }

    @GetMapping("/total-revenue")
    public ResponseEntity<?> totalRevenue() {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        statisticsInterface.totalRevenuee())
        );
    }

    @GetMapping("/total-revenue/by-userid")
    public ResponseEntity<?> totalRevenueByUserId() {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        statisticsInterface.totalRevenueByUserId())
        );
    }

    @GetMapping("/count/new-order")
    public ResponseEntity<?> coutnNewOrder() {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        statisticsInterface.countNewOrder())
        );
    }

    @GetMapping("/count/customer")
    public ResponseEntity<?> countCustomer() {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        statisticsInterface.countCustomer())
        );
    }

    @GetMapping("/order/by-status")
    public ResponseEntity<?> getStats(@RequestParam String rangeType) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        statisticsInterface.getStats(rangeType))
        );
    }

    @GetMapping("/revenue/by")
    public ResponseEntity<?> getRevenue(@RequestParam String rangeType) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        statisticsInterface.getRevenueByRangeType(rangeType))
        );
    }

    @GetMapping("/stats-revenue/by")
    public ResponseEntity<?> getRvenueStatsByRangeType(@RequestParam String rangeType) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        statisticsInterface.getRevenueStatsByRangeType(rangeType))
        );
    }

    @GetMapping("/stats-order/by")
    public ResponseEntity<?> getOrderStatsByRangeType(@RequestParam String rangeType) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        statisticsInterface.getOrderStatsByRangeType(rangeType))
        );
    }

    @GetMapping("/stats-revenue/top-product")
    public ResponseEntity<?> getTopProductRevenueBetween(@RequestParam String rangeType) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        statisticsInterface.getTopProductRevenueBetween(rangeType))
        );
    }

    @GetMapping("/stats/new-customer")
    public ResponseEntity<?> getNewCustomerStatsByRangeType(@RequestParam String rangeType) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        statisticsInterface.getNewCustomerStatsByRangeType(rangeType))
        );
    }

    @GetMapping("/list/order/by")
    public ResponseEntity<?> getPagiOrderByRangeType(@ModelAttribute PaginationRequest request, @RequestParam String rangeType) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        statisticsInterface.getPagiOrderByRangeType(request, rangeType))
        );
    }
}
