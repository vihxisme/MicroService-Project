package com.service.discount.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import com.service.discount.entities.Discount;
import com.service.discount.requests.DiscountRequest;
import com.service.discount.resources.DiscountResource;

@Mapper(componentModel = "spring", uses = {
    DiscountTypeConvert.class }, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface DiscountMapper {
  DiscountMapper INSTANCE = Mappers.getMapper(DiscountMapper.class);

  @Mapping(target = "discountType", source = "discountType", qualifiedByName = "idToDiscountType")
  Discount toDiscount(DiscountRequest request);

  @Mapping(target = "discountType", source = "discountType", qualifiedByName = "idToDiscountType")
  void updateDiscountFromRequest(DiscountRequest request, @MappingTarget Discount discount);

  DiscountResource toDiscountResource(Discount discount);
}
