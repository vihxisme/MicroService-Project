package com.service.order.wrappers;

import java.util.List;

import com.service.order.requests.OrderItemRequest;
import com.service.order.requests.OrderRequest;
import com.service.order.requests.ShippingAddressRequest;

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
public class OrderWrapper {

    private OrderRequest orderRequest;
    private List<OrderItemRequest> orderItemRequests;
    private ShippingAddressRequest shippingAddressRequest;
    private Integer paymentMethod;
}
