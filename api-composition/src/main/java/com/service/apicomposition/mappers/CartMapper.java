package com.service.apicomposition.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.service.apicomposition.resources.CartItemProdResource;
import com.service.apicomposition.resources.CartItemResource;
import com.service.apicomposition.resources.ProdAndStatusResource;
import com.service.apicomposition.resources.ProdVariantResource;

@Mapper(componentModel = "spring")
public interface CartMapper {

    @Mapping(source = "cartItem.id", target = "id")
    @Mapping(source = "cartItem.cartId", target = "cartId")
    @Mapping(source = "cartItem.productId", target = "productId")
    @Mapping(source = "cartItem.prodVariantId", target = "prodVariantId")
    @Mapping(source = "cartItem.quantity", target = "quantity")
    @Mapping(source = "variant.colorId", target = "colorId")
    @Mapping(source = "variant.colorName", target = "colorName")
    @Mapping(source = "variant.sizeId", target = "sizeId")
    @Mapping(source = "variant.sizeName", target = "sizeName")
    @Mapping(source = "productStatus.productName", target = "productName")
    @Mapping(source = "productStatus.status", target = "productStatus")
    @Mapping(target = "price", expression = "java(java.math.BigDecimal.ZERO)")
    CartItemProdResource toCartItemProdResource(
            CartItemResource cartItem,
            ProdVariantResource variant,
            ProdAndStatusResource productStatus
    );
}
