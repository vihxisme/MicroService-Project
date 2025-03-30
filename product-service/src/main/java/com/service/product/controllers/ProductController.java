package com.service.product.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.service.product.requests.PaginationRequest;
import com.service.product.requests.ProductRequest;
import com.service.product.responses.SuccessResponse;
import com.service.product.services.interfaces.ProductInterface;
import com.service.product.wrapper.ProductWrapper;

@RestController
@RequestMapping("/v1/products")
public class ProductController {

    @Autowired
    private ProductInterface productInterface;

    @PostMapping("/create")
    public ResponseEntity<?> createProduct(@RequestBody ProductWrapper request) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        productInterface.create(request)));
    }

    @PatchMapping("/update")
    public ResponseEntity<?> updateProduct(@RequestBody ProductRequest request) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        productInterface.updateProduct(request)));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable String id) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        productInterface.deleteProduct(UUID.fromString(id))));
    }

    @GetMapping("/info/all")
    public ResponseEntity<?> getAllProduct(@ModelAttribute PaginationRequest request) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        productInterface.getAllProduct(request)));
    }

    // lấy ra danh sách sản phẩm kèm khuyến mãi (nếu có)
    @GetMapping("/with-discount")
    public ResponseEntity<?> getProductWithDiscount(@ModelAttribute PaginationRequest request) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        productInterface.getProductWithDiscount(request)));
    }

    // lấy danh sách sản phẩm theo danh mục
    @GetMapping("/by-categorie")
    public ResponseEntity<?> getProductWithDiscountbyCategorie(@RequestParam String categorieId, @ModelAttribute PaginationRequest request) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        productInterface.getProductWithDiscountByCategorie(categorieId, request)));
    }

    // lấy ra danh sách sản phẩm đang được khuyến mãi
    @GetMapping("/only-discount")
    public ResponseEntity<?> getOnlyProductDiscount(@ModelAttribute PaginationRequest request) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        productInterface.getOnlyProductDiscount(request)));
    }

    @GetMapping("/new")
    public ResponseEntity<?> getNewProducts(@ModelAttribute PaginationRequest request) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        productInterface.getNewProducts(request))
        );
    }

}
