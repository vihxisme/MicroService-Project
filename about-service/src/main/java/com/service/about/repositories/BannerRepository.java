package com.service.about.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.service.about.entities.Banner;

@Repository
public interface BannerRepository extends JpaRepository<Banner, Integer> {

}
