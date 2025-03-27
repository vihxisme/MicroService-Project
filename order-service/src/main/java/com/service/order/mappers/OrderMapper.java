package com.service.order.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.service.order.entities.Order;
import com.service.order.requests.OrderRequest;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface OrderMapper {

    @Mapping(target = "totalAmount", source = "totalAmount", defaultValue = "0")
    Order toOrder(OrderRequest request);

    @Mapping(target = "totalAmount", source = "totalAmount", defaultValue = "0")
    void updateOrderFromRequest(OrderRequest request, @MappingTarget Order order);
}
