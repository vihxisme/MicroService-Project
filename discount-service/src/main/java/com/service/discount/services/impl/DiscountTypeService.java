package com.service.discount.services.impl;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.service.discount.entities.DiscountType;
import com.service.discount.mappers.DiscountTypeMapper;
import com.service.discount.repositories.DiscountTypeRepository;
import com.service.discount.requests.TypeRequest;
import com.service.discount.resources.TypeResource;
import com.service.discount.services.interfaces.DiscountTypeInterface;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class DiscountTypeService implements DiscountTypeInterface {

  @Autowired
  private DiscountTypeRepository discountTypeRepository;

  @Autowired
  private DiscountTypeMapper discountTypeMapper;

  @Override
  @Transactional
  public DiscountType createDiscountType(TypeRequest request) {
    if (request.getTypeCode() == null) {
      request.setTypeCode(generateTypeCode());
    }

    DiscountType discountType = discountTypeMapper.toDiscountType(request);

    return discountTypeRepository.save(discountType);
  }

  @Override
  @Transactional
  public DiscountType updateDiscountType(TypeRequest request) {
    DiscountType existDiscountType = discountTypeRepository.findById(request.getId())
        .orElseThrow(() -> new EntityNotFoundException("Discount type not found"));

    discountTypeMapper.updateDiscountTypeFromRequest(request, existDiscountType);

    return discountTypeRepository.save(existDiscountType);
  }

  @Override
  @Transactional
  public Boolean deleteDiscountType(Long id) {
    Boolean isType = discountTypeRepository.deleteByIdCustom(id) > 0;

    if (isType) {
      return true;
    } else {
      throw new EntityNotFoundException("Discount type not found");
    }
  }

  @Override
  public List<TypeResource> getDiscountTypes() {
    List<DiscountType> discountTypes = discountTypeRepository.findAll();

    List<TypeResource> typeResources = discountTypes.stream()
        .map(discountTypeMapper::toTypeResource)
        .toList();

    return typeResources;
  }

  public String generateTypeCode() {
    Boolean isUnique;
    Random random = new Random();
    String code;
    do {
      int randomNumberStart = random.nextInt(10, 99);
      int randomNumberMiddle = random.nextInt(100, 999);
      int randomNumberEnd = random.nextInt(100, 999);
      code = String.format("%02d-%03d-%03d", randomNumberStart, randomNumberMiddle, randomNumberEnd);
      isUnique = !discountTypeRepository.existsByTypeCode(code);
    } while (!isUnique);

    return code;
  }

}
