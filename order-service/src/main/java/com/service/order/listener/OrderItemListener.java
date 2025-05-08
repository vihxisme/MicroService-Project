package com.service.order.listener;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.service.events.dtos.InvenItemStockDTO;
import com.service.order.entities.OrderItem;
import com.service.order.listener.events.OrderItemListChangedEvent;
import com.service.order.services.impl.RabbitService;

@Component
public class OrderItemListener {

    @Autowired
    private RabbitService rabbitService;

    @Value("${rabbitmq.exchange.inventory}")
    private String inventoryExchange;

    private Logger logger = LoggerFactory.getLogger(OrderItemListener.class);

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onOrderItemListChanged(OrderItemListChangedEvent event) {
        List<OrderItem> orderItemList = event.getOrderItems();

        if (orderItemList == null || orderItemList.isEmpty()) {
            return;
        }

        Map<Integer, Integer> updateInvenItemMap = orderItemList.stream()
                .collect(Collectors.toMap(
                        OrderItem::getProdVariantId,
                        OrderItem::getQuantity,
                        (existing, replacement) -> existing + replacement
                ));

        List<InvenItemStockDTO> invenItemDTOs = orderItemList.stream()
                .map(orderItem -> InvenItemStockDTO.builder()
                .orderId(orderItem.getOrders().getId())
                .orderCode(orderItem.getOrders().getOrderCode())
                .prodVariantId(orderItem.getProdVariantId())
                .itemQuantity(orderItem.getQuantity())
                .build())
                .collect(Collectors.toList());

        rabbitService.sendMap(inventoryExchange, "update-inventory-item", updateInvenItemMap);
        rabbitService.sendMessage(inventoryExchange, "inventory-item-stock", invenItemDTOs);
    }
}
