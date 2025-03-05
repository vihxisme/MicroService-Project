package com.service.product.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.service.product.entities.Categorie;
import com.service.product.entities.Product;
import com.service.product.resources.ProdAndStatusResource;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    boolean existsByProductCode(String code);

    Page<Product> findAll(Pageable pageable);

    @Query("""
      SELECT p FROM Product p WHERE p.status != 'INACTIVE'
      """)
    List<Product> findAllElseInactive();

    @Query("""
      SELECT p FROM Product p WHERE p.categorie = :categorie
      """)
    Page<Product> findAllByCategorie(@Param("categorie") Categorie categorie, Pageable pageable);

    @Query("""
      SELECT p FROM Product p WHERE p.status != 'INACTIVE'
      """)
    Page<Product> findAllElseInactive(Pageable pageable);

    @Query("""
        SELECT new com.service.product.resources.ProdAndStatusResource(
            p.id,
            p.name,
            CAST(p.status AS String)
        )
        FROM
            Product p
        WHERE 
            p.id IN :ids
          """)
    List<ProdAndStatusResource> findProdAndStatusByProdId(@Param("ids") List<UUID> ids);
}
