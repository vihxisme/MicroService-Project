package com.service.discount.services.interfaces;

import com.service.discount.entities.DiscountTarget;
import com.service.discount.requests.PaginationRequest;
import com.service.discount.requests.TargetRequest;
import com.service.discount.responses.PaginationResponse;

public interface DiscountTargetInterface {
  DiscountTarget createDiscountTarget(TargetRequest request);

  DiscountTarget updateDiscountTarget(TargetRequest request);

  Boolean deleteDiscountTarget(Long id);

  PaginationResponse<DiscountTarget> getAllDiscountTargets(PaginationRequest request);
}