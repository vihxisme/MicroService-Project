package com.service.product.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.service.product.requests.PaginationRequest;
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

}
