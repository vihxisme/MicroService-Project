package com.service.product.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import com.service.product.entities.ProductImage;
import com.service.product.requests.ProductImageRequest;

@Mapper(componentModel = "spring", uses = {
    Convert.class }, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductImageMapper {
  ProductImageMapper INSTANCE = Mappers.getMapper(ProductImageMapper.class);

  @Mapping(target = "product", source = "productId", qualifiedByName = "UUIDToProduct")
  ProductImage toProductImgage(ProductImageRequest request);

  @Mapping(target = "product", source = "productId", qualifiedByName = "UUIDToProduct")
  void updateProductImageFromRequest(ProductImageRequest request, @MappingTarget ProductImage productImage);
}
