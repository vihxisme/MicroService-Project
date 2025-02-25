package com.service.product.services.interfaces;

import java.util.List;
import java.util.UUID;

import com.service.product.entities.ProductVariant;
import com.service.product.requests.ProductVariantRequest;
import com.service.product.requests.VariantRequest;
import com.service.product.resources.ColorResource;
import com.service.product.resources.SizeResource;

public interface ProductVariantInterface {
  List<ProductVariant> createProductVariant(ProductVariantRequest request, VariantRequest variantRequest);

  ProductVariant updateProductVariant(ProductVariantRequest request);

  Boolean deleteProductVariant(Integer id);

  List<SizeResource> getSizesFromVariant(UUID productId);

  List<ColorResource> getColorsFromVariant(UUID productId);

  List<SizeResource> getSizeFromVariantForProduct(ProductVariantRequest request);

  List<ProductVariant> getVariantByProductIdAndColorId(ProductVariantRequest request);
}
