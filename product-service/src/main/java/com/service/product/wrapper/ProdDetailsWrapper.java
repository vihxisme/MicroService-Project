package com.service.product.wrapper;

import java.util.List;

import com.service.product.requests.ProductDetailRequest;

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
public class ProdDetailsWrapper {

    private List<ProductDetailRequest> prodDetailsList;
}
