package com.service.discount.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import com.service.discount.entities.DiscountType;
import com.service.discount.requests.TypeRequest;
import com.service.discount.resources.TypeResource;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface DiscountTypeMapper {
  DiscountTypeMapper INSTANCE = Mappers.getMapper(DiscountTypeMapper.class);

  DiscountType toDiscountType(TypeRequest typeRequest);

  void updateDiscountTypeFromRequest(TypeRequest typeRequest, @MappingTarget DiscountType discountType);

  TypeResource toTypeResource(DiscountType discountType);
}
