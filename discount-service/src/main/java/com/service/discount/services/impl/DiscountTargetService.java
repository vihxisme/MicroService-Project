package com.service.discount.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.service.discount.entities.DiscountTarget;
import com.service.discount.mappers.DiscountTargetMapper;
import com.service.discount.repositories.DiscountTargetRepository;
import com.service.discount.requests.PaginationRequest;
import com.service.discount.requests.TargetRequest;
import com.service.discount.responses.PaginationResponse;
import com.service.discount.services.interfaces.DiscountTargetInterface;

import jakarta.transaction.Transactional;

@Service
public class DiscountTargetService implements DiscountTargetInterface {

  @Autowired
  private DiscountTargetRepository discountTargetRepository;

  @Autowired
  private DiscountTargetMapper discountTargetMapper;

  @Override
  @Transactional
  public DiscountTarget createDiscountTarget(TargetRequest request) {
    DiscountTarget discountTarget = discountTargetMapper.toDiscountTarget(request);

    return discountTargetRepository.save(discountTarget);
  }

  @Override
  @Transactional
  public DiscountTarget updateDiscountTarget(TargetRequest request) {
    DiscountTarget existDiscountTarget = discountTargetRepository.findById(request.getId())
        .orElseThrow(() -> new RuntimeException("Discount target not found"));

    existDiscountTarget = discountTargetMapper.updateDiscountTargetFromRequest(request, existDiscountTarget);

    return discountTargetRepository.save(existDiscountTarget);
  }

  @Override
  @Transactional
  public Boolean deleteDiscountTarget(Long id) {
    Boolean isDiscountTarget = discountTargetRepository.deleteByIdCustom(id) > 0;

    if (!isDiscountTarget) {
      throw new RuntimeException("Discount target not found");
    }

    return isDiscountTarget;
  }

  @Override
  public PaginationResponse<DiscountTarget> getAllDiscountTargets(PaginationRequest request) {
    Pageable pageable = PageRequest.of(request.getPage(), request.getSize());

    Page<DiscountTarget> discountTargets = discountTargetRepository.findAll(pageable);

    return PaginationResponse.<DiscountTarget>builder()
        .content(discountTargets.getContent())
        .pageNumber(discountTargets.getNumber())
        .pageSize(discountTargets.getSize())
        .totalPages(discountTargets.getTotalPages())
        .totalElements(discountTargets.getTotalElements())
        .build();
  }

}
