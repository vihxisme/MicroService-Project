package com.service.product.services.interfaces;

import java.util.List;
import java.util.UUID;

import com.service.product.entities.ProductDetail;
import com.service.product.requests.ProductDetailRequest;

public interface ProductDetailInterface {
  List<ProductDetail> createProductDetailList(List<ProductDetailRequest> requests);

  List<ProductDetail> updateProductDetailsList(List<ProductDetailRequest> requests);

  Boolean deleteProductDetailList(List<Integer> ids);

  List<ProductDetail> getProductDetailById(UUID productId);
}
