package com.service.product.services.impl;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.service.product.entities.Categorie;
import com.service.product.entities.Product;
import com.service.product.mappers.ProductMapper;
import com.service.product.repositories.CategorieRepository;
import com.service.product.repositories.ProductRepository;
import com.service.product.requests.PaginationRequest;
import com.service.product.requests.ProductRequest;
import com.service.product.resources.ProductResource;
import com.service.product.responses.PaginationResponse;
import com.service.product.services.interfaces.ProductInterface;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class ProductService implements ProductInterface {

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private CategorieRepository categorieRepository;

  @Autowired
  private ProductMapper productMapper;

  @Override
  @Transactional
  public Product createProduct(ProductRequest request) {
    if (request.getProductCode() == null) {
      request.setProductCode(generateProductCode());
    }

    Product product = productMapper.toProduct(request);

    return productRepository.save(product);
  }

  @Override
  @Transactional
  public Product updateProduct(ProductRequest request) {
    Product existProduct = productRepository.findById(request.getId())
        .orElseThrow(() -> new EntityNotFoundException("Product not found"));

    productMapper.updateProductFromRequest(request, existProduct);

    return productRepository.save(existProduct);
  }

  @Override
  @Transactional
  public Boolean deleteProduct(UUID id) {
    Product existProduct = productRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Product not found"));

    productRepository.delete(existProduct);

    return true;
  }

  private String generateProductCode() {
    Boolean isUnique;
    Random random = new Random();
    String code;
    do {
      int randomNumberStart = random.nextInt(100, 999);
      int randomNumberMiddle = random.nextInt(10000, 99999);
      int randomNumberEnd = random.nextInt(10000, 99999);
      code = String.format("%03d-%05d-%05d", randomNumberStart, randomNumberMiddle, randomNumberEnd);
      isUnique = !productRepository.existsByProductCode(code);
    } while (!isUnique);

    return code;
  }

  @Override
  public PaginationResponse<Product> getAllProduct(PaginationRequest request) {
    Pageable pageable = PageRequest.of(request.getPage(), request.getSize());

    Page<Product> prosducts = productRepository.findAll(pageable);

    return PaginationResponse.<Product>builder()
        .content(prosducts.getContent())
        .pageNumber(prosducts.getNumber())
        .pageSize(prosducts.getSize())
        .totalPages(prosducts.getTotalPages())
        .totalElements(prosducts.getTotalElements())
        .build();
  }

  @Override
  public List<ProductResource> getAllProductElseInactive() {
    List<Product> products = productRepository.findAllElseInactive();

    List<ProductResource> productResources = products.stream()
        .map(productMapper::toProductResource)
        .collect(Collectors.toList());

    return productResources;
  }

  @Override
  public PaginationResponse<ProductResource> getAllProductByCategorie(UUID categoriId, PaginationRequest request) {
    Categorie categorie = categorieRepository.findById(categoriId)
        .orElseThrow(() -> new EntityNotFoundException("Categorie not found"));

    Pageable pageable = PageRequest.of(request.getPage(), request.getSize());

    Page<Product> productsByCategorie = productRepository.findAllByCategorie(categorie, pageable);

    Page<ProductResource> productResourcesByCategorie = productsByCategorie.map(productMapper::toProductResource);

    return PaginationResponse.<ProductResource>builder()
        .content(productResourcesByCategorie.getContent())
        .pageNumber(productResourcesByCategorie.getNumber())
        .pageSize(productResourcesByCategorie.getSize())
        .totalPages(productResourcesByCategorie.getTotalPages())
        .totalElements(productResourcesByCategorie.getTotalElements())
        .build();
  }

  @Override
  public Map<UUID, String> getProductName(List<UUID> productIds) {
    return productRepository.findAllById(productIds)
        .stream()
        .collect(Collectors.toMap(Product::getId, Product::getName));
  }

  @Override
  public PaginationResponse<ProductResource> getAllProductElseInactive(PaginationRequest request) {
    Pageable pageable = PageRequest.of(request.getPage(), request.getSize());

    Page<Product> products = productRepository.findAllElseInactive(pageable);

    Page<ProductResource> productResources = products.map(productMapper::toProductResource);

    return PaginationResponse.<ProductResource>builder()
        .content(productResources.getContent())
        .pageNumber(productResources.getNumber())
        .pageSize(productResources.getSize())
        .totalPages(productResources.getTotalPages())
        .totalElements(productResources.getTotalElements())
        .build();
  }

}
