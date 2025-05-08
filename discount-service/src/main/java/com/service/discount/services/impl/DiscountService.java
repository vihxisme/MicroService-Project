package com.service.discount.services.impl;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.service.discount.entities.Discount;
import com.service.discount.enums.TargetTypeEnum;
import com.service.discount.mappers.DiscountMapper;
import com.service.discount.repositories.DiscountRepository;
import com.service.discount.repositories.DiscountTargetRepository;
import com.service.discount.requests.DiscountRequest;
import com.service.discount.requests.PaginationRequest;
import com.service.discount.resources.DiscountClientResource;
import com.service.discount.resources.DiscountResource;
import com.service.discount.resources.DiscountStatisticsResource;
import com.service.discount.resources.DiscountWithTargetResource;
import com.service.discount.responses.PaginationResponse;
import com.service.discount.services.interfaces.DiscountInterface;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class DiscountService implements DiscountInterface {

    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private DiscountTargetRepository discountTargetRepository;

    @Autowired
    private DiscountMapper discountMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private Logger logger = LoggerFactory.getLogger(DiscountService.class);

    @Override
    @Transactional
    public Discount createDiscount(DiscountRequest request) {
        if (request.getDiscountCode() == null) {
            request.setDiscountCode(generateDiscountCode());
        }

        request.setDiscountType(3);
        Discount discount = discountMapper.toDiscount(request);
        Discount savedDiscount = discountRepository.save(discount);

        rabbitTemplate.convertAndSend("cache-update.exchange", "clear-cache", "*");
        return savedDiscount;
    }

    @Override
    @Transactional
    public Discount updateDiscount(DiscountRequest request) {
        Discount existDiscount = discountRepository.findById(request.getId())
                .orElseThrow(() -> new EntityNotFoundException("Discount not found"));

        discountMapper.updateDiscountFromRequest(request, existDiscount);

        Discount savedDiscount = discountRepository.save(existDiscount);

        rabbitTemplate.convertAndSend("cache-update.exchange", "clear-cache", "*");
        return savedDiscount;
    }

    @Override
    @Transactional
    public Boolean deleteDiscount(UUID id) {
        Discount existDiscount = discountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Discount not found"));

        discountRepository.delete(existDiscount);

        rabbitTemplate.convertAndSend("cache-update.exchange", "clear-cache", "*");
        return true;
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

    @Override
    public List<DiscountClientResource> getAllDiscountsClient() {
        return discountRepository.getAllDiscountsClient();
    }

    @Override
    public PaginationResponse<DiscountClientResource> getDiscountsWithTarget(PaginationRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());

        Page<DiscountClientResource> discountsWithTarget = discountRepository.getDiscountsWithTarget(pageable);

        return PaginationResponse.<DiscountClientResource>builder()
                .content(discountsWithTarget.getContent())
                .pageNumber(discountsWithTarget.getNumber())
                .pageSize(discountsWithTarget.getSize())
                .totalPages(discountsWithTarget.getTotalPages())
                .totalElements(discountsWithTarget.getTotalElements())
                .build();
    }

    @Override
    public PaginationResponse<DiscountWithTargetResource> getDiscountWithTargets(PaginationRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());

        Page<DiscountWithTargetResource> discountWithTargets = discountRepository.getDiscountWithTargets(pageable);

        return PaginationResponse.<DiscountWithTargetResource>builder()
                .content(discountWithTargets.getContent())
                .pageNumber(discountWithTargets.getNumber())
                .pageSize(discountWithTargets.getSize())
                .totalPages(discountWithTargets.getTotalPages())
                .totalElements(discountWithTargets.getTotalElements())
                .build();
    }

    @Override
    public DiscountClientResource getByTargetIdDiscountClientResource(UUID targetId) {
        return discountRepository.getByTargetIWithDiscountsClient(targetId);
    }

    @Override
    public PaginationResponse<DiscountWithTargetResource> getDiscountWithTargets(PaginationRequest request,
            UUID discountId, String targetType) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), Sort.by("createdAt").descending());

        TargetTypeEnum typeEnums = TargetTypeEnum.valueOf(targetType);

        Page<DiscountWithTargetResource> discountsWithTarget = discountRepository.getDiscountWithTargets(pageable, discountId, typeEnums);

        return PaginationResponse.<DiscountWithTargetResource>builder()
                .content(discountsWithTarget.getContent())
                .pageNumber(discountsWithTarget.getNumber())
                .pageSize(discountsWithTarget.getSize())
                .totalPages(discountsWithTarget.getTotalPages())
                .totalElements(discountsWithTarget.getTotalElements())
                .build();
    }

    @Override
    public DiscountStatisticsResource discountStatisticsResource() {
        return DiscountStatisticsResource.builder()
                .totalDiscounts(discountRepository.countAllDiscounts())
                .activeDiscounts(discountRepository.countActiveDiscounts())
                .discountedProducts(discountTargetRepository.countDiscountedProducts())
                .discountedCategories(discountTargetRepository.countDiscountedCategories())
                .build();
    }
}
