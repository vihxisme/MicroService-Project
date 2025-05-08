package com.service.order.wrappers;

import com.service.order.entities.Order;

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
public class CheckInventoryWrapper {

    private OrderWrapper orderWrapper;
    private Order order;
}
