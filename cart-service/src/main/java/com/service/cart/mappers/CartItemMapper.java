package com.service.cart.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.service.cart.entities.CartItem;
import com.service.cart.requests.CartItemRequest;

@Mapper(componentModel = "spring", uses = {Convert.class}, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CartItemMapper {

    @Mapping(target = "cart", source = "cartId", qualifiedByName = "uuidToCart")
    CartItem toCartItem(CartItemRequest request);

    @Mapping(target = "cart", source = "cartId", qualifiedByName = "uuidToCart")
    void updateCartItemToRequest(CartItemRequest request, @MappingTarget CartItem cartItem);
}
