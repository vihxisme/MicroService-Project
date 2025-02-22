package com.service.discount.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import com.service.discount.entities.DiscountTarget;
import com.service.discount.requests.TargetRequest;

@Mapper(componentModel = "spring", uses = {
    DiscountConvert.class }, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface DiscountTargetMapper {
  DiscountTargetMapper INSTANCE = Mappers.getMapper(DiscountTargetMapper.class);

  @Mapping(target = "discount", source = "discountId", qualifiedByName = "UUIDToDiscount")
  DiscountTarget toDiscountTarget(TargetRequest request);

  @Mapping(target = "discount", source = "discountId", qualifiedByName = "UUIDToDiscount")
  DiscountTarget updateDiscountTargetFromRequest(TargetRequest request, @MappingTarget DiscountTarget discountTarget);

}
