package com.service.about.services.interfaces;

import java.util.List;

import com.service.about.entities.Banner;
import com.service.about.requests.BannerRequest;

public interface BannerInterface {

    List<Banner> createBanner(List<BannerRequest> requests);

    List<Banner> updateBanner(List<BannerRequest> requests);

    Boolean deleteBanner(List<Integer> ids);

    List<Banner> getAllBanners();
}
