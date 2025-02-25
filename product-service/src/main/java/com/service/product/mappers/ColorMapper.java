package com.service.product.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import com.service.product.entities.Color;
import com.service.product.requests.ColorRequest;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ColorMapper {
  ColorMapper INSTANCE = Mappers.getMapper(ColorMapper.class);

  Color toColor(ColorRequest request);

  void updateColorFromRequest(ColorRequest request, @MappingTarget Color color);
}
