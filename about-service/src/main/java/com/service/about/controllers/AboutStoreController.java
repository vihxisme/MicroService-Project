package com.service.about.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.about.requests.AboutStoreRequest;
import com.service.about.responses.SuccessResponse;
import com.service.about.services.interfaces.AboutStoreInterface;

@RestController
@RequestMapping("/v1/about-store")
public class AboutStoreController {

    @Autowired
    private AboutStoreInterface aboutStoreInterface;

    @PostMapping("/create")
    public ResponseEntity<?> createAboutStore(@RequestBody AboutStoreRequest request) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        aboutStoreInterface.createAboutStore(request))
        );
    }

    @PatchMapping("/update")
    public ResponseEntity<?> updateAboutStore(@RequestBody AboutStoreRequest request) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        aboutStoreInterface.updateAboutStore(request))
        );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAboutStore(@PathVariable Integer id) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        aboutStoreInterface.deleteAboutStore(id))
        );
    }

    @GetMapping("/list-all")
    public ResponseEntity<?> getAllAboutStore() {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        aboutStoreInterface.getAllAboutStore())
        );
    }
}
