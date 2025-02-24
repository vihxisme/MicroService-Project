package com.service.discount.services.interfaces;

import java.util.List;
import java.util.UUID;

import com.service.discount.entities.Discount;
import com.service.discount.requests.DiscountRequest;
import com.service.discount.requests.PaginationRequest;
import com.service.discount.resources.DiscountClientResource;
import com.service.discount.resources.DiscountResource;
import com.service.discount.responses.PaginationResponse;

public interface DiscountInterface {
  Discount createDiscount(DiscountRequest request);

  Discount updateDiscount(DiscountRequest request);

  Boolean deleteDiscount(UUID id);

  PaginationResponse<DiscountResource> getAllDiscounts(PaginationRequest request);

  List<DiscountClientResource> getAllDiscountsClient();
}
