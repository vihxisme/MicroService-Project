package com.service.discount.mappers;

import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.service.discount.entities.DiscountType;
import com.service.discount.repositories.DiscountTypeRepository;

@Component
public class DiscountTypeConvert {
  @Autowired
  private DiscountTypeRepository discountTypeRepository;

  @Named("idToDiscountType")
  public DiscountType idToDiscountType(Long discountType) {
    return discountTypeRepository.findById(discountType)
        .orElseThrow(() -> new RuntimeException("Discount type not found"));
  }
}
