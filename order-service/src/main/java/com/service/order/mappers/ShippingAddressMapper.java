package com.service.order.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.service.order.entities.ShippingAddress;
import com.service.order.requests.ShippingAddressRequest;

@Mapper(componentModel = "spring", uses = {Convert.class}, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ShippingAddressMapper {

    @Mapping(target = "orders", source = "orderId", qualifiedByName = "uuidToOrder")
    ShippingAddress toShippingAddress(ShippingAddressRequest request);

    @Mapping(target = "orders", source = "orderId", qualifiedByName = "uuidToOrder")
    void updateShippingAddressFromRequest(ShippingAddressRequest request, @MappingTarget ShippingAddress shippingAddress);
}
