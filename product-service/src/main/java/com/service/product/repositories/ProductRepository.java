package com.service.product.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
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
      SELECT p FROM Product p WHERE p.status != 'INACTIVE' ORDER BY p.createdAt DESC
      """)
    List<Product> findAllElseInactive();

    @Query("""
      SELECT p FROM Product p WHERE p.categorie = :categorie ORDER BY p.createdAt DESC
      """)
    Page<Product> findAllByCategorie(@Param("categorie") Categorie categorie, Pageable pageable);

    @Query("""
      SELECT p FROM Product p WHERE p.status != 'INACTIVE' ORDER BY p.createdAt DESC
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

    @Query("""
      SELECT p FROM Product p WHERE p.categorie.apparelType = :apparelType ORDER BY p.createdAt DESC
      """)
    Page<Product> findByCateApparelType(@Param("apparelType") Integer apparelType, Pageable pageable);

    @EntityGraph(attributePaths = {"productVariants.color", "productVariants.size", "productImages", "productDetails"})
    @Query("""
        SELECT p FROM Product p WHERE p.id = :id AND p.status != 'INACTIVE' ORDER BY p.createdAt DESC
        """)
    Product findByIdWithProductAllInfo(@Param("id") UUID id);
}
