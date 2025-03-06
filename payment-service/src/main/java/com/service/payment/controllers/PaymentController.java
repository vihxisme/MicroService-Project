package com.service.payment.controllers;

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

import com.service.payment.requests.PaymentRequest;
import com.service.payment.responses.SuccessResponse;
import com.service.payment.services.interfaces.PaymentInterface;

@RestController
@RequestMapping("/v1/payment")
public class PaymentController {

    @Autowired
    private PaymentInterface paymentInterface;

    @PostMapping("/create")
    public ResponseEntity<?> createPayment(@RequestBody PaymentRequest request) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        paymentInterface.createPayment(request))
        );
    }

    @PatchMapping("/update")
    public ResponseEntity<?> updatePayment(@RequestBody PaymentRequest request) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        paymentInterface.updatePayment(request))
        );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePayment(@PathVariable String id) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        paymentInterface.deletePayment(UUID.fromString(id)))
        );
    }
}
