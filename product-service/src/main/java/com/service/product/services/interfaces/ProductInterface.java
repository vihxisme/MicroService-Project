package com.service.product.services.interfaces;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.service.product.dtos.ProductInvenDTO;
import com.service.product.entities.Product;
import com.service.product.requests.PaginationRequest;
import com.service.product.requests.ProdSendEmailRequest;
import com.service.product.requests.ProductRequest;
import com.service.product.requests.ProductVariantRequest;
import com.service.product.resources.ProdAllInfoResource;
import com.service.product.resources.ProdAndStatusResource;
import com.service.product.resources.ProdIdNameResource;
import com.service.product.resources.ProdSendEmailResource;
import com.service.product.resources.ProdWithDiscountAllInfoResource;
import com.service.product.resources.ProductResource;
import com.service.product.resources.ProductWithDiscountResource;
import com.service.product.resources.TopProductResource;
import com.service.product.responses.PaginationResponse;
import com.service.product.wrapper.ProductWrapper;

public interface ProductInterface {

    Product create(ProductWrapper productWrapper);

    Long countProduct();

    Product createProduct(ProductRequest request);

    Product updateProduct(ProductRequest request);

    Boolean deleteProduct(UUID id);

    Product getProductById(UUID id);

    PaginationResponse<ProductResource> getProductsByPagination(PaginationRequest request);

    List<ProductResource> getAllProducts();

    List<ProductResource> getAllProductElseInactive();

    PaginationResponse<ProductResource> getAllProductByCategorie(UUID categoriId, PaginationRequest request);

    Map<UUID, String> getProductName(List<UUID> productIds);

    PaginationResponse<ProductResource> getAllProductElseInactive(PaginationRequest request);

    PaginationResponse<ProductWithDiscountResource> getProductWithDiscount(PaginationRequest request);

    PaginationResponse<ProductWithDiscountResource> getProductWithDiscountByCategorie(String categorieId, PaginationRequest request);

    PaginationResponse<ProductWithDiscountResource> getOnlyProductDiscount(PaginationRequest request);

    List<ProdAndStatusResource> getProdAndStatus(List<UUID> ids);

    PaginationResponse<ProductWithDiscountResource> getNewProducts(PaginationRequest request);

    PaginationResponse<ProductResource> getProdByCateApparelType(Integer apparelType, PaginationRequest request);

    PaginationResponse<ProductWithDiscountResource> getProdWithDiscountByCateApparelType(Integer apparelType, PaginationRequest request);

    ProdAllInfoResource getProductAllInfoById(UUID id);

    ProdWithDiscountAllInfoResource getProdWithDiscountAllInfoById(UUID id);

    Map<UUID, ProdIdNameResource> getProdIdNameById(List<UUID> productIds);

    Map<UUID, ProductInvenDTO> getProductInven(List<UUID> productIds);

    Map<UUID, ProductResource> getProductByIds(List<UUID> productIds);

    List<ProductWithDiscountResource> getTopProductWithDiscount(Integer limit);

    List<TopProductResource> getTopProductResource(Integer limit);

    List<TopProductResource> getTopProductResource(String rangeType, Integer limit);

    List<ProdSendEmailResource> getProdSendEmail(List<ProdSendEmailRequest> requests);
}
