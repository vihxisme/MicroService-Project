package com.service.product.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import com.service.product.entities.ProductDetail;
import com.service.product.requests.ProductDetailRequest;

@Mapper(componentModel = "spring", uses = {
    Convert.class }, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductDetailMapper {
  ProductDetailMapper INSTANCE = Mappers.getMapper(ProductDetailMapper.class);

  @Mapping(target = "product", source = "productId", qualifiedByName = "UUIDToProduct")
  ProductDetail toProductDetail(ProductDetailRequest request);

  @Mapping(target = "product", source = "productId", qualifiedByName = "UUIDToProduct")
  void updateProductDetailFromRequest(ProductDetailRequest request, @MappingTarget ProductDetail productDetail);
}
