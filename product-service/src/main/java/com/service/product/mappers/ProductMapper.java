package com.service.product.mappers;

import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import com.service.product.dtos.ProductDetailDTO;
import com.service.product.dtos.ProductImageDTO;
import com.service.product.dtos.ProductVariantDTO;
import com.service.product.entities.Product;
import com.service.product.entities.ProductDetail;
import com.service.product.entities.ProductImage;
import com.service.product.entities.ProductVariant;
import com.service.product.requests.ProductRequest;
import com.service.product.resources.ProdAllInfoResource;
import com.service.product.resources.ProductResource;

@Mapper(componentModel = "spring", uses = {
    Convert.class}, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mapping(target = "categorie", source = "categorieId", qualifiedByName = "toCategorie")
    @Mapping(target = "status", source = "status", qualifiedByName = "fromString")
    Product toProduct(ProductRequest request);

    @Mapping(target = "categorie", source = "categorieId", qualifiedByName = "toCategorie")
    @Mapping(target = "status", source = "status", qualifiedByName = "fromString")
    void updateProductFromRequest(ProductRequest request, @MappingTarget Product product);

    @Mapping(target = "categorieId", source = "categorie", qualifiedByName = "categorieToUUID")
    @Mapping(target = "status", source = "status", qualifiedByName = "toStringFromStatusEnum")
    @Mapping(target = "categoryName", source = "categorie.name")
    ProductResource toProductResource(Product product);

    // Map từ Product sang ProductDTO
    @Mapping(source = "productVariants", target = "productVariantsDTO")
    @Mapping(source = "productImages", target = "productImagesDTO")
    @Mapping(source = "productDetails", target = "productDetailsDTO")
    @Mapping(target = "categorieId", source = "categorie", qualifiedByName = "categorieToUUID")
    @Mapping(target = "status", source = "status", qualifiedByName = "toStringFromStatusEnum")
    ProdAllInfoResource toProdAllInfoResource(Product product);

    // Map từ ProductVariant sang ProductVariantDTO
    @Mapping(source = "color.id", target = "colorId")
    @Mapping(source = "color.name", target = "colorName")
    @Mapping(source = "size.id", target = "sizeId")
    @Mapping(source = "size.name", target = "sizeName")
    ProductVariantDTO toProductVariantDTO(ProductVariant variant);

    // Map từ ProductImage sang ProductImageDTO
    @Mapping(source = "id", target = "id")
    @Mapping(source = "imageUrl", target = "imageUrl")
    ProductImageDTO toProductImageDTO(ProductImage image);

    // Map từ ProductDetail sang ProductDetailDTO
    @Mapping(source = "id", target = "id")
    @Mapping(source = "attributeName", target = "attributeName")
    @Mapping(source = "attributeValue", target = "attributeValue")
    ProductDetailDTO toProductDetailDTO(ProductDetail detail);

    // Map danh sách ProductVariant → ProductVariantDTO
    default Set<ProductVariantDTO> mapVariants(Set<ProductVariant> variants) {
        return variants.stream()
                .map(this::toProductVariantDTO)
                .collect(Collectors.toSet());
    }

    // Map danh sách ProductImage → ProductImageDTO
    default Set<ProductImageDTO> mapImages(Set<ProductImage> images) {
        return images.stream()
                .map(this::toProductImageDTO)
                .collect(Collectors.toSet());
    }

    // Map danh sách ProductDetail → ProductDetailDTO
    default Set<ProductDetailDTO> mapDetails(Set<ProductDetail> details) {
        return details.stream()
                .map(this::toProductDetailDTO)
                .collect(Collectors.toSet());

    }
}
