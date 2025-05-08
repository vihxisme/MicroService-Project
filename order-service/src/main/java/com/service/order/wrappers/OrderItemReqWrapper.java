package com.service.order.wrappers;

import java.util.List;

import com.service.order.requests.OrderItemRequest;

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
public class OrderItemReqWrapper {

    private List<OrderItemRequest> orderItemRequestList;
}
