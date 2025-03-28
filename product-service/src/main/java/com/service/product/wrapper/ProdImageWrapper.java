package com.service.product.wrapper;

import java.util.List;

import com.service.product.requests.ProductImageRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ProdImageWrapper {

    private List<ProductImageRequest> prodImageList;
}
