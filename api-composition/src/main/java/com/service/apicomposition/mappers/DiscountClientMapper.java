package com.service.apicomposition.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.service.apicomposition.resources.DiscountWithTarget;
import com.service.apicomposition.resources.DiscountWithTargetNameResource;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface DiscountClientMapper {

  DiscountWithTargetNameResource toDiscountWithTargetNameResource(DiscountWithTarget discountWithTarget,
      String targetName);
}
