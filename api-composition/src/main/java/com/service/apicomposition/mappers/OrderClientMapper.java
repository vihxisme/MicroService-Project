package com.service.apicomposition.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.service.apicomposition.resources.OrderItemProdResource;
import com.service.apicomposition.resources.OrderItemResource;
import com.service.apicomposition.resources.ProdIdNameResource;
import com.service.apicomposition.resources.ProdVariantColorSizeResource;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface OrderClientMapper {

    OrderItemProdResource toOrderItemProdResource(
            OrderItemResource orderItemResource,
            ProdIdNameResource prodIdNameResource,
            ProdVariantColorSizeResource prodVariantColorSizeResource
    );
}
