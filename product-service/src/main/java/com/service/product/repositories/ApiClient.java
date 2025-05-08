package com.service.product.repositories;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "api-composition")
public interface ApiClient {

    @GetMapping("product-client/with-discount")
    ResponseEntity<?> getProductWithDiscount(@RequestParam int page, @RequestParam int size);

    @GetMapping("/product-client/by-categorie")
    ResponseEntity<?> getProductWithDiscountByCategorie(@RequestParam String categorieId, @RequestParam int page, @RequestParam int size);

    @GetMapping("/product-client/only-discount")
    ResponseEntity<?> getOnlyProductDiscount(@RequestParam int page, @RequestParam int size);

    @GetMapping("/product-client/apparel-type")
    ResponseEntity<?> getProdWithDiscountByCateApparel(@RequestParam Integer apparelType, @RequestParam int page, @RequestParam int size);

    @GetMapping("/product-client/detail-info")
    ResponseEntity<?> getProdWithDiscountAllInfoById(@RequestParam UUID id);

    @GetMapping("/product-client/top-product/by-revenue")
    ResponseEntity<?> getTopProduct(@RequestParam Integer limit);

    @GetMapping("/product-client/top-product/with-discount")
    ResponseEntity<?> getTopProductWithDiscount(@RequestParam Integer limit);

    @GetMapping("/product-client/top-product/by-rangetype")
    ResponseEntity<?> getTopProduct(@RequestParam Integer limit, @RequestParam String rangeType);

}
