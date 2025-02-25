package com.service.product.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import com.service.product.entities.Product;
import com.service.product.requests.ProductRequest;
import com.service.product.resources.ProductResource;

@Mapper(componentModel = "spring", uses = {
    Convert.class }, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductMapper {
  ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

  @Mapping(target = "categorie", source = "categorieId", qualifiedByName = "toCategorie")
  @Mapping(target = "status", source = "status", qualifiedByName = "fromString")
  Product toProduct(ProductRequest request);

  @Mapping(target = "categorie", source = "categorieId", qualifiedByName = "toCategorie")
  @Mapping(target = "status", source = "status", qualifiedByName = "fromString")
  void updateProductFromRequest(ProductRequest request, @MappingTarget Product product);

  @Mapping(target = "categorieId", source = "categorie", qualifiedByName = "categorieToUUID")
  @Mapping(target = "status", source = "status", qualifiedByName = "toStringFromStatusEnum")
  ProductResource toProductResource(Product product);
}
