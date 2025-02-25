package com.service.product.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.service.product.entities.Categorie;

@Repository
public interface CategorieRepository extends JpaRepository<Categorie, UUID> {

  boolean existsByCategorieCode(String code);

  Page<Categorie> findAll(Pageable pageable);
}
