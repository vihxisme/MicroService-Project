package com.service.product.services.interfaces;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.service.product.entities.ProductVariant;
import com.service.product.requests.ProductVariantRequest;
import com.service.product.requests.VariantRequest;
import com.service.product.resources.ColorResource;
import com.service.product.resources.ProdVariantColorSizeResource;
import com.service.product.resources.ProdVariantResource;
import com.service.product.resources.SizeResource;

public interface ProductVariantInterface {

    List<ProductVariant> createProductVariant(VariantRequest request);

    ProductVariant updateProductVariant(ProductVariantRequest request);

    Boolean deleteProductVariant(Integer id);

    List<SizeResource> getSizesFromVariant(UUID productId);

    List<ColorResource> getColorsFromVariant(UUID productId);

    List<SizeResource> getSizeFromVariantForProduct(UUID productId, Integer colorId);

    List<ProductVariant> getVariantByProductIdAndColorId(UUID productId, Integer colorId);

    List<ProdVariantResource> getProdVariantById(List<Integer> ids);

    Map<Integer, ProdVariantColorSizeResource> getProdVariantColorSizeById(List<Integer> variantIds);
}
