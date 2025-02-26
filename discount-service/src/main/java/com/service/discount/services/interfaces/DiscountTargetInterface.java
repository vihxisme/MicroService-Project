package com.service.discount.services.interfaces;

import com.service.discount.entities.DiscountTarget;
import com.service.discount.requests.PaginationRequest;
import com.service.discount.requests.TargetRequest;
import com.service.discount.resources.DiscountWithTargetNameResource;
import com.service.discount.responses.PaginationResponse;

public interface DiscountTargetInterface {
  DiscountTarget createDiscountTarget(TargetRequest request);

  DiscountTarget updateDiscountTarget(TargetRequest request);

  Boolean deleteDiscountTarget(Integer id);

  PaginationResponse<DiscountTarget> getAllDiscountTargets(PaginationRequest request);

  PaginationResponse<DiscountWithTargetNameResource> getDiscountWithTargetName(PaginationRequest request);
}