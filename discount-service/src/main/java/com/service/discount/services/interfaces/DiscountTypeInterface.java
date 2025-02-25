package com.service.discount.services.interfaces;

import java.util.List;

import com.service.discount.entities.DiscountType;
import com.service.discount.requests.TypeRequest;
import com.service.discount.resources.TypeResource;

public interface DiscountTypeInterface {
  DiscountType createDiscountType(TypeRequest request);

  DiscountType updateDiscountType(TypeRequest request);

  Boolean deleteDiscountType(Long id);

  List<TypeResource> getDiscountTypes();
}
