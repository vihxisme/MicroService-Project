package com.service.product.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.service.product.entities.Product;
import com.service.product.entities.ProductImage;
import com.service.product.mappers.ProductImageMapper;
import com.service.product.repositories.ProductImageRepository;
import com.service.product.repositories.ProductRepository;
import com.service.product.requests.ProductImageRequest;
import com.service.product.services.interfaces.ProductImageInterface;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class ProductImageService implements ProductImageInterface {

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductImageMapper productImageMapper;

    private Logger logger = LoggerFactory.getLogger(ProductImageService.class);

    // @RabbitListener(bindings = @QueueBinding(
    //         value = @Queue(name = "create-prod-images", durable = "true"),
    //         exchange = @Exchange(name = "create-prod-images-exchange", type = "direct"),
    //         key = "create-prod-images"
    // ))
    // public void createProdImageListener(List<ProductImageRequest> requests) {
    //     createProductImage(requests);
    // }
    @Override
    @Transactional
    public List<ProductImage> createProductImage(List<ProductImageRequest> requests) {
        if (requests == null || requests.isEmpty()) {
            throw new IllegalArgumentException("List of requests cannot be null or empty");
        }

        List<ProductImage> productImages = new ArrayList<>();

        for (ProductImageRequest request : requests) {
            ProductImage productImage = productImageMapper.toProductImgage(request);
            productImages.add(productImage);
        }

        return productImageRepository.saveAll(productImages);
    }

    @Override
    @Transactional
    public List<ProductImage> updateProductImage(List<ProductImageRequest> requests) {
        if (requests == null || requests.isEmpty()) {
            throw new IllegalArgumentException("List of requests cannot be null or empty");
        }

        List<ProductImage> existProductImages = new ArrayList<>();

        for (ProductImageRequest request : requests) {
            ProductImage productImage = productImageRepository.findById(request.getId())
                    .orElseThrow(() -> new EntityNotFoundException("ProductImage not found"));

            productImageMapper.updateProductImageFromRequest(request, productImage);

            existProductImages.add(productImage);
        }

        return productImageRepository.saveAll(existProductImages);
    }

    @Override
    @Transactional
    public Boolean deleteProductImage(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException("List of requests cannot be null of empty");
        }

        List<ProductImage> existProductImages = productImageRepository.findAllById(ids);

        if (existProductImages.size() != ids.size()) {
            throw new EntityNotFoundException("ProductImage not found");
        }

        productImageRepository.deleteAll(existProductImages);

        return true;
    }

    @Override
    public List<ProductImage> getProductImageByProduct(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        return productImageRepository.findByProduct(product);
    }

}
