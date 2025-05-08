package com.service.about.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.service.about.entities.Banner;
import com.service.about.mappers.BannerMapper;
import com.service.about.repositories.BannerRepository;
import com.service.about.requests.BannerRequest;
import com.service.about.services.interfaces.BannerInterface;

import jakarta.persistence.EntityNotFoundException;

@Service
public class BannerService implements BannerInterface {

    @Autowired
    private BannerRepository bannerRepository;

    @Autowired
    private BannerMapper bannerMapper;

    @Override
    public List<Banner> createBanner(List<BannerRequest> requests) {
        if (requests == null || requests.isEmpty()) {
            throw new IllegalArgumentException("requests is empty");
        }

        List<Banner> banners = new ArrayList<>();

        for (BannerRequest request : requests) {
            Banner banner = bannerMapper.toBanner(request);
            banners.add(banner);
        }

        return bannerRepository.saveAll(banners);
    }

    @Override
    public List<Banner> updateBanner(List<BannerRequest> requests) {
        if (requests == null || requests.isEmpty()) {
            throw new IllegalArgumentException("requests is empty");
        }

        List<Banner> banners = new ArrayList<>();

        for (BannerRequest request : requests) {
            Banner existBanner = bannerRepository.findById(request.getId()).orElseThrow(
                    () -> new EntityNotFoundException("Banner not found")
            );

            bannerMapper.updateBannerFromDto(existBanner, request);
            banners.add(existBanner);
        }

        return bannerRepository.saveAll(banners);
    }

    @Override
    public Boolean deleteBanner(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException("requests is empty");
        }

        bannerRepository.deleteAllById(ids);

        return true;
    }

    @Override
    public List<Banner> getAllBanners() {
        return bannerRepository.findAll();
    }
}
