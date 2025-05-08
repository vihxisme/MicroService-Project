package com.service.about.controllers;

import java.util.List;

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

import com.service.about.requests.BannerRequest;
import com.service.about.responses.SuccessResponse;
import com.service.about.services.interfaces.BannerInterface;

@RestController
@RequestMapping("/v1/banner")
public class BannerController {

    @Autowired
    private BannerInterface bannerInterface;

    @PostMapping("/create")
    public ResponseEntity<?> createBanner(@RequestBody List<BannerRequest> requests) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        bannerInterface.createBanner(requests))
        );
    }

    @PatchMapping("/update")
    public ResponseEntity<?> updateBanner(@RequestBody List<BannerRequest> requests) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        bannerInterface.updateBanner(requests))
        );
    }

    @DeleteMapping("/delete/{ids}")
    public ResponseEntity<?> deleteBanner(@PathVariable List<Integer> ids) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        bannerInterface.deleteBanner(ids))
        );
    }

    @GetMapping("/list-all")
    public ResponseEntity<?> getAllBanner() {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        bannerInterface.getAllBanners())
        );
    }
}
