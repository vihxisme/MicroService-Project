package com.service.product.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.service.product.entities.ProductVariant;
import com.service.product.resources.ColorResource;
import com.service.product.resources.ProdVariantResource;
import com.service.product.resources.SizeResource;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Integer> {

    Optional<ProductVariant> findByProductIdAndColorIdAndSizeId(UUID productId, Integer colorId, Integer sizeId);

    @Query("""
      SELECT DISTINCT new com.service.product.resources.ColorResource(
        c.id,
        c.name
      )
        FROM ProductVariant pv
        JOIN pv.color c
        WHERE pv.product.id = :productId
      """)
    List<ColorResource> findDistinctColorsByProductId(@Param("productId") UUID productId);

    @Query("""
      SELECT DISTINCT new com.service.product.resources.SizeResource(
        s.id,
        s.name
      )
        FROM ProductVariant pv
        JOIN pv.size s
        WHERE pv.product.id = :productId
      """)
    List<SizeResource> findDistinctSizesByProductId(@Param("productId") UUID productId);

    @Query("""
      SELECT DISTINCT new com.service.product.resources.SizeResource(
        s.id,
        s.name
      )
        FROM ProductVariant pv
        JOIN pv.size s
        JOIN pv.color c
        WHERE pv.product.id = :productId AND pv.color.id = :colorId AND pv.stock > 0
      """)
    List<SizeResource> findSizeByProductIdAndColor(@Param("productId") UUID productId, @Param("colorId") Integer colorId);

    @Query("""
      SELECT pv
      FROM ProductVariant pv
      WHERE pv.product.id = :productId AND pv.color.id = :colorId AND pv.stock > 0
      """)
    List<ProductVariant> findAllByProductIdAndColor(@Param("productId") UUID productId,
            @Param("colorId") Integer colorId);

    @Query("""
          SELECT new com.service.product.resources.ProdVariantResource(
            pv.id,
            c.id,
            c.name,
            s.id,
            s.name
          )
          FROM
            ProductVariant pv
            JOIN pv.color c
            JOIN pv.size s
          WHERE pv.id IN :ids
          """)
    List<ProdVariantResource> findProdVariantById(@Param("ids") List<Integer> ids);
}
