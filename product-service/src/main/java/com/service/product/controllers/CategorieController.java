package com.service.product.controllers;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.product.requests.CategorieRequest;
import com.service.product.requests.PaginationRequest;
import com.service.product.responses.SuccessResponse;
import com.service.product.services.interfaces.CategorieInterface;

@RestController
@RequestMapping("/v1/categorie")
public class CategorieController {

    @Autowired
    private CategorieInterface categorieInterface;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private final String messageUpdate = "refresh";

    @PostMapping("/create")
    public ResponseEntity<?> createCategorie(@RequestBody CategorieRequest request) {
        SuccessResponse<?> response = new SuccessResponse<>("SUCCESS", categorieInterface.createCategorie(request));
        Optional.ofNullable(response.getData())
                .ifPresent(data -> notifyUpdateCategory(messageUpdate));
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/update")
    public ResponseEntity<?> updateCategorie(@RequestBody CategorieRequest request) {
        SuccessResponse<?> response = new SuccessResponse<>("SUCCESS", categorieInterface.updateCategorie(request));
        Optional.ofNullable(response.getData())
                .ifPresent(data -> notifyUpdateCategory(messageUpdate));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCategorie(@PathVariable String id) {
        SuccessResponse<?> response = new SuccessResponse<>("SUCCESS", categorieInterface.deleteCategorie(UUID.fromString(id)));
        Optional.ofNullable(response.getData())
                .ifPresent(data -> notifyUpdateCategory(messageUpdate));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/info/all")
    public ResponseEntity<?> getAllCategorie() {
        return ResponseEntity.ok(new SuccessResponse<>("SUCCESS", categorieInterface.getAllCategorie()));
    }

    @GetMapping("/info/all/list")
    public ResponseEntity<?> getAllCategorie(@ModelAttribute PaginationRequest request) {
        return ResponseEntity.ok(new SuccessResponse<>("SUCCESS", categorieInterface.getAllCategorie(request)));
    }

    private void notifyUpdateCategory(String ms) {
        // Gửi thông báo đến tất cả client lắng nghe
        messagingTemplate.convertAndSend("/topic/category-updated", ms);
    }
}
