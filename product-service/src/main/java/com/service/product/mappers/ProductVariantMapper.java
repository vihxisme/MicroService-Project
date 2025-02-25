package com.service.product.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import com.service.product.entities.ProductVariant;
import com.service.product.requests.ProductVariantRequest;

@Mapper(componentModel = "spring", uses = {
    Convert.class }, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductVariantMapper {
  ProductVariantMapper INSTANCE = Mappers.getMapper(ProductVariantMapper.class);

  @Mapping(target = "product", source = "productId", qualifiedByName = "UUIDToProduct")
  @Mapping(target = "size", source = "sizeId", qualifiedByName = "idToSize")
  @Mapping(target = "color", source = "colorId", qualifiedByName = "idToColor")
  ProductVariant toProductVariant(ProductVariantRequest request);

  @Mapping(target = "product", source = "productId", qualifiedByName = "UUIDToProduct")
  @Mapping(target = "size", source = "sizeId", qualifiedByName = "idToSize")
  @Mapping(target = "color", source = "colorId", qualifiedByName = "idToColor")
  void updateProductVariantFromRequest(ProductVariantRequest request, @MappingTarget ProductVariant variant);
}
