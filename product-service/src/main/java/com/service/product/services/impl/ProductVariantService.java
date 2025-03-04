package com.service.product.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.service.product.entities.ProductVariant;
import com.service.product.mappers.ProductVariantMapper;
import com.service.product.repositories.ProductVariantRepository;
import com.service.product.requests.ProductVariantRequest;
import com.service.product.requests.VariantRequest;
import com.service.product.resources.ColorResource;
import com.service.product.resources.ProdVariantResource;
import com.service.product.resources.SizeResource;
import com.service.product.services.interfaces.ProductVariantInterface;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class ProductVariantService implements ProductVariantInterface {

    @Autowired
    private ProductVariantRepository productVariantRepository;

    @Autowired
    private ProductVariantMapper productVariantMapper;

    private Logger logger = LoggerFactory.getLogger(ProductVariantService.class);

    @Override
    @Transactional
    public List<ProductVariant> createProductVariant(VariantRequest request) {
        List<ProductVariant> productVariantList = new ArrayList<>();

        logger.info("color: " + request.getColorIds().size());
        logger.info("size: " + request.getSizeIds().size());
        logger.info("image: " + request.getColorImageUrls().size());

        for (Integer i = 0; i < request.getColorIds().size(); i++) {
            String colorImage = (i < request.getColorImageUrls().size() && request.getColorImageUrls().get(i) != null)
                    ? request.getColorImageUrls().get(i) : null;
            logger.info("colorImage: " + colorImage);
            for (Integer size : request.getSizeIds()) {
                ProductVariantRequest productVariantRequest = ProductVariantRequest.builder()
                        .productId(request.getProductId())
                        .colorId(request.getColorIds().get(i))
                        .sizeId(size)
                        .colorImageUrl(colorImage)
                        .build();
                ProductVariant productVariant = productVariantMapper.toProductVariant(productVariantRequest);

                productVariantList.add(productVariant);
            }
        }

        return productVariantRepository.saveAll(productVariantList);
    }

    @Override
    @Transactional
    public ProductVariant updateProductVariant(ProductVariantRequest request) {
        ProductVariant existProductVariant = productVariantRepository.findById(request.getId())
                .orElseThrow(() -> new EntityNotFoundException("ProductVariant not found"));

        productVariantMapper.updateProductVariantFromRequest(request, existProductVariant);

        return productVariantRepository.save(existProductVariant);
    }

    @Override
    public Boolean deleteProductVariant(Integer id) {
        ProductVariant existProductVariant = productVariantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ProductVariant not found"));

        productVariantRepository.delete(existProductVariant);

        return true;
    }

    @Override
    public List<SizeResource> getSizesFromVariant(UUID productId) {
        return productVariantRepository.findDistinctSizesByProductId(productId);
    }

    @Override
    public List<ColorResource> getColorsFromVariant(UUID productId) {
        return productVariantRepository.findDistinctColorsByProductId(productId);
    }

    @Override
    public List<SizeResource> getSizeFromVariantForProduct(UUID productId, Integer colorId) {
        return productVariantRepository.findSizeByProductIdAndColor(productId, colorId);
    }

    @Override
    public List<ProductVariant> getVariantByProductIdAndColorId(UUID productId, Integer colorId) {
        return productVariantRepository.findAllByProductIdAndColor(productId, colorId);
    }

    @Override
    public List<ProdVariantResource> getProdVariantById(List<Integer> ids) {
        return productVariantRepository.findAllById(ids).stream()
                .map(productVariant -> new ProdVariantResource(
                productVariant.getId(),
                productVariant.getColor().getId(),
                productVariant.getColor().getName(),
                productVariant.getSize().getId(),
                productVariant.getSize().getName()))
                .collect(Collectors.toList());

    }

}
