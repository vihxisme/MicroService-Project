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

    @GetMapping("/info/list")
    public ResponseEntity<?> getAllProducts() {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        productInterface.getAllProducts()));
    }

    @GetMapping("/info/all")
    public ResponseEntity<?> getProductsByPagination(@ModelAttribute PaginationRequest request) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        productInterface.getProductsByPagination(request)));
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

    // lấy ra danh sách sản phẩm mới
    @GetMapping("/new")
    public ResponseEntity<?> getNewProducts(@ModelAttribute PaginationRequest request) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        productInterface.getNewProducts(request))
        );
    }

    // lấy ra danh sách sản phẩm với mã giảm giá theo apparelType của categorie
    @GetMapping("/apparel-type")
    public ResponseEntity<?> getProdWithDiscountByCateApparelType(@RequestParam Integer apparelType, @ModelAttribute PaginationRequest request) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        productInterface.getProdWithDiscountByCateApparelType(apparelType, request))
        );
    }

    @GetMapping("/detail-info")
    public ResponseEntity<?> getProductDetail(@RequestParam UUID id) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        productInterface.getProdWithDiscountAllInfoById(id))
        );
    }

    @GetMapping("/by")
    public ResponseEntity<?> getProductById(@RequestParam UUID id) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        productInterface.getProductAllInfoById(id))
        );
    }

    @GetMapping("/count/product")
    public ResponseEntity<?> countProduct() {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        productInterface.countProduct())
        );
    }

    @GetMapping("/top-product/by-revenue")
    public ResponseEntity<?> getTopProduct(@RequestParam Integer limit) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        productInterface.getTopProductResource(limit)
                )
        );
    }

    @GetMapping("/top-product/with-discount")
    public ResponseEntity<?> getTopProductWithDiscount(@RequestParam Integer limit) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        productInterface.getTopProductWithDiscount(limit)
                )
        );
    }

    @GetMapping("/top-product/by-rangetype")
    public ResponseEntity<?> getTopProduct(@RequestParam Integer limit, @RequestParam String rangeType) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        productInterface.getTopProductResource(rangeType, limit))
        );
    }
}
