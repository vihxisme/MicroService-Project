package com.service.customer.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.customer.entities.Address;
import com.service.customer.requests.AddAddressRequest;
import com.service.customer.requests.UpdateAddrerssRequest;
import com.service.customer.responses.ErrorResponse;
import com.service.customer.responses.SuccessResponse;
import com.service.customer.services.interfaces.AddressInterface;

@RestController
@RequestMapping("/v1/address")
public class AddressController {

    @Autowired
    private AddressInterface addressInterface;

    @PostMapping("/add")
    public ResponseEntity<?> addAddress(@RequestBody AddAddressRequest request) {
        Address address = addressInterface.addAddress(request);

        SuccessResponse<Address> success = new SuccessResponse<>("SUCCESS", address);
        return ResponseEntity.status(HttpStatus.CREATED).body(success);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateAddress(@RequestBody UpdateAddrerssRequest request) {
        Address address = addressInterface.updateAddress(request);

        SuccessResponse<Address> success = new SuccessResponse<>("SUCCESS", address);
        return ResponseEntity.ok().body(success);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAddress(@PathVariable Long id) {
        Boolean isAddress = addressInterface.deleteAddress(id);

        if (isAddress) {
            SuccessResponse<Boolean> success = new SuccessResponse<>("SUCCESS", isAddress);
            return ResponseEntity.ok().body(success);
        } else {
            Map<String, String> errors = new HashMap<>();
            errors.put("message", "Error: Data not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(404, "NOT_FOUND", errors));
        }
    }

    @GetMapping("/info/{customerId}")
    public ResponseEntity<?> getAddress(@PathVariable String customerId) {
        List<Address> list = addressInterface.getAllAddressByCustomer(UUID.fromString(customerId));

        SuccessResponse<List<Address>> success = new SuccessResponse<>("SUCCESS", list);
        return ResponseEntity.ok().body(success);
    }
}
