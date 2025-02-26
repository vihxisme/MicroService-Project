package com.service.discount.mappers;

import java.util.UUID;

import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.service.discount.entities.Discount;
import com.service.discount.entities.DiscountType;
import com.service.discount.enums.TargetTypeEnum;
import com.service.discount.repositories.DiscountRepository;
import com.service.discount.repositories.DiscountTypeRepository;

@Component
public class Convert {
  @Autowired
  private DiscountRepository discountRepository;

  @Autowired
  private DiscountTypeRepository discountTypeRepository;

  @Named("idToDiscountType")
  public DiscountType idToDiscountType(Integer discountType) {
    return discountTypeRepository.findById(discountType)
        .orElseThrow(() -> new RuntimeException("Discount type not found"));
  }

  @Named("UUIDToDiscount")
  public Discount UUIDToDiscount(UUID id) {
    return discountRepository.findById(id).orElseThrow(() -> new RuntimeException("Discount not found"));
  }

  @Named("fromString")
  public TargetTypeEnum fromString(String type) {
    return TargetTypeEnum.valueOf(type);
  }

}
