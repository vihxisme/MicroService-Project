package com.service.order.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.order.requests.ShippingAddressRequest;
import com.service.order.responses.SuccessResponse;
import com.service.order.services.interfaces.ShippingAddressInterface;

@RestController
@RequestMapping("/v1/shipping-address")
public class ShippingAddressController {

    @Autowired
    private ShippingAddressInterface shippingAddressInterface;

    @PostMapping("/create")
    public ResponseEntity<?> createShippingAddress(@RequestBody ShippingAddressRequest request) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        shippingAddressInterface.createShippingAddress(request))
        );
    }

    @PatchMapping("/update")
    public ResponseEntity<?> updateShippingAddress(@RequestBody ShippingAddressRequest request) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        shippingAddressInterface.updateShippingAddress(request))
        );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteShippingAddress(@PathVariable Integer id) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        shippingAddressInterface.deleteShippingAddress(id))
        );
    }
}
