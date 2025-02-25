package com.service.product.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.service.product.entities.Product;
import com.service.product.entities.ProductDetail;
import com.service.product.mappers.ProductDetailMapper;
import com.service.product.repositories.ProductDetailRepository;
import com.service.product.repositories.ProductRepository;
import com.service.product.requests.ProductDetailRequest;
import com.service.product.services.interfaces.ProductDetailInterface;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class ProductDetailService implements ProductDetailInterface {

  @Autowired
  private ProductDetailRepository productDetailRepository;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private ProductDetailMapper productDetailMapper;

  private Logger logger = LoggerFactory.getLogger(ProductDetailService.class);

  @Override
  @Transactional
  public List<ProductDetail> createProductDetailList(List<ProductDetailRequest> requests) {
    if (requests == null || requests.isEmpty()) {
      throw new IllegalArgumentException("List of requests cannot be null or empty");
    }

    List<ProductDetail> productDetails = new ArrayList<>();

    for (ProductDetailRequest request : requests) {
      ProductDetail productDetail = productDetailMapper.toProductDetail(request);
      productDetails.add(productDetail);
    }

    return productDetailRepository.saveAll(productDetails);
  }

  @Override
  @Transactional
  public List<ProductDetail> updateProductDetailsList(List<ProductDetailRequest> requests) {

    if (requests == null || requests.isEmpty()) {
      throw new IllegalArgumentException("List of requests cannot be null or empty");
    }

    List<ProductDetail> existProductDetails = new ArrayList<>();

    for (ProductDetailRequest request : requests) {
      ProductDetail existProductDetail = productDetailRepository.findById(request.getId())
          .orElseThrow(() -> new EntityNotFoundException("ProductDetail not found"));

      productDetailMapper.updateProductDetailFromRequest(request, existProductDetail);

      existProductDetails.add(existProductDetail);
    }

    return productDetailRepository.saveAll(existProductDetails);
  }

  @Override
  @Transactional
  public Boolean deleteProductDetailList(List<Integer> ids) {
    if (ids == null || ids.isEmpty()) {
      throw new IllegalArgumentException("List of IDs cannot be null or empty");
    }

    List<ProductDetail> existProductDetails = productDetailRepository.findAllById(ids);

    if (existProductDetails.size() != ids.size()) {
      // Tìm các ID không tồn tại trong cơ sở dữ liệu
      // List<Integer> existingIds = existProductDetails.stream()
      // .map(ProductDetail::getId)
      // .collect(Collectors.toList());

      // List<Integer> missingIds = ids.stream()
      // .filter(id -> !existingIds.contains(id))
      // .collect(Collectors.toList());

      throw new EntityNotFoundException("ProductDetail not found");
    }

    productDetailRepository.deleteAll(existProductDetails);

    return true;
  }

  @Override
  public List<ProductDetail> getProductDetailById(UUID productId) {
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new EntityNotFoundException("Product not found"));

    return productDetailRepository.findByProduct(product);
  }

  @Override
  public ProductDetail create(ProductDetailRequest request) {
    ProductDetail productDetail = productDetailMapper.toProductDetail(request);

    // logger.info("id: " + productDetail.getId());
    // logger.info("name: " + productDetail.getAttributeName());
    // logger.info("value: " + productDetail.getAttributeValue());

    // // return productDetailRepository.save(productDetail);

    // ProductDetail savedProductDetail =
    // productDetailRepository.save(productDetail);

    // logger.info("Saved productDetail: " + savedProductDetail);
    // return savedProductDetail;
    try {
      ProductDetail savedProductDetail = productDetailRepository.save(productDetail);
      logger.info("Saved productDetail: " + savedProductDetail);
      return savedProductDetail;
    } catch (Exception e) {
      logger.error("Error while saving productDetail: ", e);
      throw e;
    }
  }

}
