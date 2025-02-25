package com.service.product.mappers;

import org.mapstruct.factory.Mappers;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.service.product.entities.Categorie;
import com.service.product.requests.CategorieRequest;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CategorieMapper {
  CategorieMapper INSTANCE = Mappers.getMapper(CategorieMapper.class);

  Categorie toCategorie(CategorieRequest request);

  void updateCategorieFromRequest(CategorieRequest request, @MappingTarget Categorie categorie);
}
