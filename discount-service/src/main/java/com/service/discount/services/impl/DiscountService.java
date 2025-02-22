package com.service.discount.services.impl;

import java.util.Random;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.service.discount.entities.Discount;
import com.service.discount.mappers.DiscountMapper;
import com.service.discount.repositories.DiscountRepository;
import com.service.discount.requests.DiscountRequest;
import com.service.discount.requests.PaginationRequest;
import com.service.discount.resources.DiscountResource;
import com.service.discount.responses.PaginationResponse;
import com.service.discount.services.interfaces.DiscountInterface;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class DiscountService implements DiscountInterface {

  @Autowired
  private DiscountRepository discountRepository;

  @Autowired
  private DiscountMapper discountMapper;

  private Logger logger = LoggerFactory.getLogger(DiscountService.class);

  @Override
  @Transactional
  public Discount createDiscount(DiscountRequest request) {
    if (request.getDiscountCode() == null) {
      request.setDiscountCode(generateDiscountCode());
    }

    Discount discount = discountMapper.toDiscount(request);

    return discountRepository.save(discount);
  }

  @Override
  @Transactional
  public Discount updateDiscount(DiscountRequest request) {
    Discount existDiscount = discountRepository.findById(request.getId())
        .orElseThrow(() -> new EntityNotFoundException("Discount not found"));

    discountMapper.updateDiscountFromRequest(request, existDiscount);

    return discountRepository.save(existDiscount);
  }

  @Override
  @Transactional
  public Boolean deleteDiscount(UUID id) {
    Boolean isDiscount = discountRepository.deleteByIdCustom(id) > 0;

    if (isDiscount) {
      return true;
    } else {
      throw new EntityNotFoundException("Discount not found");
    }
  }

  @Override
  public PaginationResponse<DiscountResource> getAllDiscounts(PaginationRequest request) {
    Pageable pageable = PageRequest.of(request.getPage(), request.getSize());

    Page<Discount> discounts = discountRepository.findAll(pageable);

    Page<DiscountResource> discountResources = discounts.map(discountMapper::toDiscountResource);

    return PaginationResponse.<DiscountResource>builder()
        .content(discountResources.getContent())
        .pageNumber(discountResources.getNumber())
        .pageSize(discountResources.getSize())
        .totalPages(discountResources.getTotalPages())
        .totalElements(discountResources.getTotalElements())
        .build();
  }

  public String generateDiscountCode() {
    Boolean isUnique;
    Random random = new Random();
    String code;
    do {
      int randomNumberStart = random.nextInt(10, 99);
      int randomNumberMiddle = random.nextInt(100, 999);
      int randomNumberEnd = random.nextInt(100, 999);
      code = String.format("%02d-%03d-%03d", randomNumberStart, randomNumberMiddle, randomNumberEnd);
      isUnique = !discountRepository.existsByDiscountCode(code);
    } while (!isUnique);

    return code;
  }

}
