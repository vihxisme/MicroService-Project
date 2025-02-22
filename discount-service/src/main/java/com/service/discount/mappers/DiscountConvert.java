package com.service.discount.mappers;

import java.util.UUID;

import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.service.discount.entities.Discount;
import com.service.discount.repositories.DiscountRepository;

@Component
public class DiscountConvert {
  @Autowired
  private DiscountRepository discountRepository;

  @Named("UUIDToDiscount")
  public Discount UUIDToDiscount(UUID id) {
    return discountRepository.findById(id).orElseThrow(() -> new RuntimeException("Discount not found"));
  }

}
