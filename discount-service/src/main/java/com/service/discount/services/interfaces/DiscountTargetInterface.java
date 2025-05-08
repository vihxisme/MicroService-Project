package com.service.discount.services.interfaces;

import java.util.List;
import java.util.UUID;

import com.service.discount.entities.DiscountTarget;
import com.service.discount.requests.PaginationRequest;
import com.service.discount.requests.TargetRequest;
import com.service.discount.resources.DiscountWithTargetNameResource;
import com.service.discount.responses.PaginationResponse;

public interface DiscountTargetInterface {

    DiscountTarget createDiscountTarget(TargetRequest request);

    List<DiscountTarget> createDiscountTarget(List<TargetRequest> requests);

    DiscountTarget updateDiscountTarget(TargetRequest request);

    Boolean deleteDiscountTarget(Integer id);

    Boolean deleteDiscountTarget(List<Integer> ids);

    PaginationResponse<DiscountTarget> getAllDiscountTargets(PaginationRequest request);

    PaginationResponse<DiscountWithTargetNameResource> getDiscountWithTargetName(PaginationRequest request);

    PaginationResponse<DiscountWithTargetNameResource> getDiscountWithTargetName(UUID discountId, String targetType, PaginationRequest request);
}
