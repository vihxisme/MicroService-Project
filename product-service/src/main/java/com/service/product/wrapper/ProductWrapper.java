package com.service.product.wrapper;

import java.util.List;

import com.service.product.requests.ProductRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProductWrapper {

    private ProductRequest productRequest;

    private List<Integer> colorIds;
    private List<Integer> sizeIds;
    private List<String> colorImageUrls;

    //productImageRequest
    private List<String> imageUrl;

    //productDetailRequest
    private List<String> attributeName;
    private List<String> attributeValue;
}
