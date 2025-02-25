package com.service.product.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import com.service.product.entities.Size;
import com.service.product.requests.SizeRequest;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SizeMapper {
  SizeMapper INSTANCE = Mappers.getMapper(SizeMapper.class);

  Size toSize(SizeRequest request);

  void updateSizeToRequest(SizeRequest request, @MappingTarget Size size);
}
