package com.service.product.services.impl;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.service.events.dto.InventoryEvent;
import com.service.events.dto.UpdateProductStatusDTO;
import com.service.events.dto.InventoryDTO;
import com.service.product.entities.Categorie;
import com.service.product.entities.Product;
import com.service.product.enums.StatusEnum;
import com.service.product.mappers.ProductMapper;
import com.service.product.repositories.ApiClient;
import com.service.product.repositories.CategorieRepository;
import com.service.product.repositories.ProductRepository;
import com.service.product.requests.PaginationRequest;
import com.service.product.requests.ProductDetailRequest;
import com.service.product.requests.ProductImageRequest;
import com.service.product.requests.ProductRequest;
import com.service.product.requests.VariantRequest;
import com.service.product.resources.ProdAndStatusResource;
import com.service.product.resources.ProductResource;
import com.service.product.resources.ProductVariantResource;
import com.service.product.resources.ProductWithDiscountResource;
import com.service.product.responses.PaginationResponse;
import com.service.product.services.interfaces.ProductInterface;
import com.service.product.wrapper.ProdDetailsWrapper;
import com.service.product.wrapper.ProdImageWrapper;
import com.service.product.wrapper.ProductWrapper;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class ProductService implements ProductInterface {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategorieRepository categorieRepository;

    @Autowired
    private ApiClient apiClient;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.product}")
    private String myExchange;

    @Value("${rabbitmq.exchange.inventory}")
    private String inventoryExchange;

    private Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Override
    @Transactional
    public Product createProduct(ProductRequest request) {
        if (request.getProductCode() == null) {
            request.setProductCode(generateProductCode());
        }

        Product product = productMapper.toProduct(request);
        Product savedProduct = productRepository.save(product);

        return savedProduct;
    }

    @Override
    public Product create(ProductWrapper productWrapper) {
        ProductRequest request = productWrapper.getProductRequest();

        Product createProduct = createProduct(request);

        if (createProduct != null) {
            VariantRequest variantRequest = VariantRequest.builder()
                    .productId(createProduct.getId())
                    .colorIds(productWrapper.getColorIds())
                    .sizeIds(productWrapper.getSizeIds())
                    .colorImageUrls(productWrapper.getColorImageUrls())
                    .build();
            List<ProductImageRequest> prodImageList = productWrapper.getImageUrl().stream()
                    .map(url -> ProductImageRequest.builder()
                    .productId(createProduct.getId())
                    .imageUrl(url)
                    .build())
                    .collect(Collectors.toList());
            List<ProductDetailRequest> prodDetailList = productWrapper.getAttributeName().stream()
                    .map(attributeName -> {
                        int index = productWrapper.getAttributeName().indexOf(attributeName);
                        return ProductDetailRequest.builder()
                                .productId(createProduct.getId())
                                .attributeName(attributeName)
                                .attributeValue(productWrapper.getAttributeValue().get(index))
                                .build();
                    })
                    .collect(Collectors.toList());

            ProdDetailsWrapper prodDetailsWrapper = ProdDetailsWrapper.builder().prodDetailsList(prodDetailList).build();
            ProdImageWrapper prodImageWrapper = ProdImageWrapper.builder().prodImageList(prodImageList).build();

            Object response = rabbitTemplate.convertSendAndReceive(myExchange, "create-prod-variant", variantRequest);
            List<ProductVariantResource> prodVariantResourceList = objectMapper.convertValue(
                    response,
                    new TypeReference<List<ProductVariantResource>>() {
            }
            );

            rabbitTemplate.convertAndSend(myExchange, "create-prod-image", prodImageWrapper);
            rabbitTemplate.convertAndSend(myExchange, "create-prod-details", prodDetailsWrapper);

            if (prodVariantResourceList != null && !prodVariantResourceList.isEmpty()) {
                InventoryDTO inventoryDTO = InventoryDTO.builder()
                        .productCode(createProduct.getProductCode())
                        .productId(createProduct.getId())
                        .isAllowed(createProduct.getStatus() != StatusEnum.INACTIVE)
                        .build();

                InventoryEvent inventoryEvent = InventoryEvent.builder()
                        .inventoryDTO(inventoryDTO)
                        .prodVariantId(prodVariantResourceList.stream().map(ProductVariantResource::getId).collect(Collectors.toList()))
                        .build();

                rabbitTemplate.convertAndSend(inventoryExchange, "create-inventory", inventoryEvent);
            } else {
                throw new IllegalArgumentException("ProdVariant is null");
            }
        }

        return createProduct;
    }

    @RabbitListener(queues = "update-product-status:queue")
    public void updateProductStatus(UpdateProductStatusDTO dto) {
        ProductRequest request = ProductRequest.builder()
                .id(dto.getProductId())
                .status(dto.getStatus())
                .build();

        updateProduct(request);
    }

    @Override
    @Transactional
    public Product updateProduct(ProductRequest request) {
        Product existProduct = productRepository.findById(request.getId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        productMapper.updateProductFromRequest(request, existProduct);
        Product savedProduct = productRepository.save(existProduct);

        return savedProduct;
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
            int randomNumberMiddle = random.nextInt(1000, 9999);
            int randomNumberEnd = random.nextInt(1000, 9999);
            code = String.format("%03d-%04d-%04d", randomNumberStart, randomNumberMiddle, randomNumberEnd);
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

    @Override
    public PaginationResponse<ProductWithDiscountResource> getProductWithDiscount(PaginationRequest request) {
        ResponseEntity<?> resource = apiClient.getProductWithDiscount(request.getPage(), request.getSize());

        PaginationResponse<ProductWithDiscountResource> paginationResponse = objectMapper.convertValue(
                resource.getBody(),
                new TypeReference<PaginationResponse<ProductWithDiscountResource>>() {
        });

        return paginationResponse;
    }

    @Override
    public PaginationResponse<ProductWithDiscountResource> getProductWithDiscountByCategorie(String categorieId,
            PaginationRequest request) {
        ResponseEntity<?> response = apiClient.getProductWithDiscountByCategorie(categorieId, request.getPage(), request.getSize());

        PaginationResponse<ProductWithDiscountResource> paginationResponse = objectMapper.convertValue(response.getBody(),
                new TypeReference<PaginationResponse<ProductWithDiscountResource>>() {
        });

        return paginationResponse;
    }

    @Override
    public PaginationResponse<ProductWithDiscountResource> getOnlyProductDiscount(PaginationRequest request) {
        ResponseEntity<?> response = apiClient.getOnlyProductDiscount(request.getPage(), request.getSize());

        PaginationResponse<ProductWithDiscountResource> paginationResponse = objectMapper.convertValue(response.getBody(),
                new TypeReference<PaginationResponse<ProductWithDiscountResource>>() {
        });

        return paginationResponse;
    }

    @Override
    public List<ProdAndStatusResource> getProdAndStatus(List<UUID> ids) {
        return productRepository.findProdAndStatusByProdId(ids);
    }

}
