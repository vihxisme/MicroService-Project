package com.service.order.listener.events;

import java.util.List;

import com.service.order.entities.OrderItem;

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
public class OrderItemListChangedEvent {

    private List<OrderItem> orderItems;

}
