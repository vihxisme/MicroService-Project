package com.service.product.services.interfaces;

import java.util.List;
import java.util.UUID;

import com.service.product.entities.Product;
import com.service.product.requests.PaginationRequest;
import com.service.product.requests.ProductRequest;
import com.service.product.resources.ProductResource;
import com.service.product.responses.PaginationResponse;

public interface ProductInterface {
  Product createProduct(ProductRequest request);

  Product updateProduct(ProductRequest request);

  Boolean deleteProduct(UUID id);

  PaginationResponse<Product> getAllProduct(PaginationRequest request);

  List<ProductResource> getAllProductElseInactive();

  List<ProductResource> getAllProductByCategorie(UUID categoriId);
}
