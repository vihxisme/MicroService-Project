package com.service.order.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.service.order.entities.OrderItem;
import com.service.order.requests.OrderItemRequest;

@Mapper(componentModel = "spring", uses = {Convert.class}, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface OrderItemMapper {

    @Mapping(target = "orders", source = "orderId", qualifiedByName = "uuidToOrder")
    OrderItem toOrderItem(OrderItemRequest request);

    @Mapping(target = "orders", source = "orderId", qualifiedByName = "uuidToOrder")
    void updateOrderItemFromRequest(OrderItemRequest request, @MappingTarget OrderItem orderItem);
}
