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
    private String email;
    private String phone;
    private String address;
    private Integer ward;
    private Integer district;
    private Integer province;
    private String fullAddress;

    @Builder.Default
    private String country = "Việt Nam";
}
