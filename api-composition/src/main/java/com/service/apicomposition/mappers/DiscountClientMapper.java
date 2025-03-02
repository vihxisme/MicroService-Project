package com.service.apicomposition.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.service.apicomposition.resources.DiscountWithTargetResource;
import com.service.apicomposition.resources.DiscountWithTargetNameResource;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface DiscountClientMapper {

    DiscountWithTargetNameResource toDiscountWithTargetNameResource(DiscountWithTargetResource discountWithTarget,
            String targetName);
}
