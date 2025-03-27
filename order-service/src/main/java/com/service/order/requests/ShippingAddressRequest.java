package com.service.order.requests;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ShippingAddressRequest {

    private Integer id;
    private UUID orderId;
    private String name;
    private String phone;
    private String street;
    private String ward;
    private String district;
    private String state;
    private String country;
}
