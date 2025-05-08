package com.service.about.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.service.about.entities.AboutStore;

@Repository
public interface AboutStoreRepository extends JpaRepository<AboutStore, Integer> {

}
