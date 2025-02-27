package com.service.apicomposition.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.service.apicomposition.resources.DiscountClientResource;
import com.service.apicomposition.resources.ProductClientResource;
import com.service.apicomposition.resources.ProductWithDiscountResource;

import jakarta.ws.rs.core.Context;

@Mapper(componentModel = "spring", uses = {
                ComponentMapper.class }, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductDiscountMapper {

        @Mapping(target = "finalPrice", expression = "java(componentMapper.calculateFinalPrice(product, discount))")
        @Mapping(target = "id", source = "product.id")
        ProductWithDiscountResource toProductWithDiscountResource(ProductClientResource product,
                        DiscountClientResource discount,
                        @Context ComponentMapper componentMapper);
}
