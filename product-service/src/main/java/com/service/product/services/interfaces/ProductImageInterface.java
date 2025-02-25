package com.service.product.services.interfaces;

import java.util.List;
import java.util.UUID;

import com.service.product.entities.ProductImage;
import com.service.product.requests.ProductImageRequest;

public interface ProductImageInterface {
  List<ProductImage> createProductImage(List<ProductImageRequest> requests);

  List<ProductImage> updateProductImage(List<ProductImageRequest> requests);

  Boolean deleteProductImage(List<Integer> ids);

  List<ProductImage> getProductImageByProduct(UUID productId);
}
