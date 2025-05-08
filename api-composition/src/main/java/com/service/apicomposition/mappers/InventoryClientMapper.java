package com.service.apicomposition.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.service.apicomposition.dtos.ProductInvenDTO;
import com.service.apicomposition.resources.InventoryClientResource;
import com.service.apicomposition.resources.InventoryProdResource;
import com.service.apicomposition.resources.ItemClientResource;
import com.service.apicomposition.resources.ItemProdVariantResource;
import com.service.apicomposition.resources.ProdVariantResource;
import com.service.apicomposition.resources.StockMvmClientResource;
import com.service.apicomposition.resources.StockMvmInProdResource;
import com.service.apicomposition.resources.StockMvmOutProdResource;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface InventoryClientMapper {

    InventoryProdResource toInventoryProductResource(InventoryClientResource inventoryClientResource, String productName);

    @Mapping(target = "productName", source = "dto.name")
    InventoryProdResource toInventoryProductResource(InventoryClientResource inventoryClientResource, ProductInvenDTO dto);

    @Mapping(target = "id", source = "stockMvmClientResource.id")
    StockMvmInProdResource toStockMvmInProdResource(
            StockMvmClientResource stockMvmClientResource,
            ProdVariantResource prodVariantResource,
            String productName);

    @Mapping(target = "id", source = "stockMvmClientResource.id")
    StockMvmOutProdResource toStockMvmOutProdResource(
            StockMvmClientResource stockMvmClientResource,
            ProdVariantResource prodVariantResource,
            String productName);

    @Mapping(target = "id", source = "itemClientResource.id")
    ItemProdVariantResource toItemProdVariantResource(ItemClientResource itemClientResource, ProdVariantResource prodVariantResource);
}
