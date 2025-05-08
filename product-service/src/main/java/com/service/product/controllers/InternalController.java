package com.service.product.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.service.product.requests.PaginationRequest;
import com.service.product.requests.ProdSendEmailRequest;
import com.service.product.requests.ProductVariantRequest;
import com.service.product.services.interfaces.CategorieInterface;
import com.service.product.services.interfaces.ProductInterface;
import com.service.product.services.interfaces.ProductVariantInterface;

@RestController
@RequestMapping("/internal")
public class InternalController {

    @Autowired
    private ProductInterface productInterface;

    @Autowired
    private CategorieInterface categorieInterface;

    @Autowired
    private ProductVariantInterface productVariantInterface;

    @GetMapping("/products/list")
    public ResponseEntity<?> getAllProductElseInactive(@ModelAttribute PaginationRequest request) {
        return ResponseEntity.ok(productInterface.getAllProductElseInactive(request));
    }

    @GetMapping("/products/categorie")
    public ResponseEntity<?> getAllProductByCategorie(@RequestParam String categorieId,
            @ModelAttribute PaginationRequest request) {
        return ResponseEntity.ok(productInterface.getAllProductByCategorie(UUID.fromString(categorieId), request));
    }

    @GetMapping("/product-names")
    public ResponseEntity<?> getProductName(@RequestParam List<UUID> productIds) {
        return ResponseEntity.ok(productInterface.getProductName(productIds));
    }

    @GetMapping("/categorie-names")
    public ResponseEntity<?> getCategorieName(@RequestParam List<UUID> categorieIds) {
        return ResponseEntity.ok(categorieInterface.getCategorieName(categorieIds));
    }

    @GetMapping("/prod-variant")
    public ResponseEntity<?> getProdVariantById(@RequestParam List<Integer> variantIds) {
        return ResponseEntity.ok(productVariantInterface.getProdVariantById(variantIds));
    }

    @GetMapping("/product-status")
    public ResponseEntity<?> getProdAndStatus(@RequestParam List<UUID> productIds) {
        return ResponseEntity.ok(productInterface.getProdAndStatus(productIds));
    }

    @GetMapping("/products/all")
    public ResponseEntity<?> getAllProduct() {
        return ResponseEntity.ok(productInterface.getAllProductElseInactive());
    }

    @GetMapping("/products/apparel-type")
    public ResponseEntity<?> getProdByCateApparelType(@RequestParam Integer apparelType, @ModelAttribute PaginationRequest request) {
        return ResponseEntity.ok(productInterface.getProdByCateApparelType(apparelType, request));
    }

    @GetMapping("/products/detail-info")
    public ResponseEntity<?> getProdAllInfoById(@RequestParam UUID id) {
        return ResponseEntity.ok(productInterface.getProductAllInfoById(id));
    }

    @GetMapping("/products/id-name/by")
    public ResponseEntity<?> getProdIdNameByProductId(@RequestParam List<UUID> productIds) {
        return ResponseEntity.ok(productInterface.getProdIdNameById(productIds));
    }

    @GetMapping("/product-variant/color-size/by")
    public ResponseEntity<?> getProdVariantColorSizeByVariantId(@RequestParam List<Integer> variantIds) {
        return ResponseEntity.ok(productVariantInterface.getProdVariantColorSizeById(variantIds));
    }

    @GetMapping("/products/inven")
    public ResponseEntity<?> getProductInven(@RequestParam List<UUID> productIds) {
        return ResponseEntity.ok(productInterface.getProductInven(productIds));
    }

    @GetMapping("/products/by")
    public ResponseEntity<?> getProductByIds(@RequestParam List<UUID> productIds) {
        return ResponseEntity.ok(productInterface.getProductByIds(productIds));
    }

    @PostMapping("/prod-send-email")
    public ResponseEntity<?> getProdSendEmail(@RequestBody List<ProdSendEmailRequest> requests) {
        return ResponseEntity.ok(productInterface.getProdSendEmail(requests));
    }
}
