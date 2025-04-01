package com.service.discount.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.service.discount.entities.Discount;
import com.service.discount.resources.DiscountClientResource;
import com.service.discount.resources.DiscountWithTargetResource;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, UUID> {

    @Modifying
    @Query("DELETE FROM Discount d WHERE d.id = :id")
    int deleteByIdCustom(@Param("id") UUID id);

    Page<Discount> findAll(Pageable pageable);

    Boolean existsByDiscountCode(String code);

    @Query("""
      SELECT new com.service.discount.resources.DiscountClientResource(
        d.id,
        d.discountPercentage,
        d.discountAmount,
        d.minOrderValue,
        CAST(dt.targetType AS string),
        dt.targetId
      )
      FROM Discount d
      JOIN d.discountTargets dt
      WHERE d.isActive=true
      """)
    List<DiscountClientResource> getAllDiscountsClient();

    @Query("""
      SELECT new com.service.discount.resources.DiscountClientResource(
        d.id,
        d.discountPercentage,
        d.discountAmount,
        d.minOrderValue,
        CAST(dt.targetType AS string),
        dt.targetId
      )
      FROM Discount d
      JOIN d.discountTargets dt
      WHERE d.isActive=true
      """)
    Page<DiscountClientResource> getDiscountsWithTarget(Pageable pageble);

    @Query("""
      SELECT new com.service.discount.resources.DiscountWithTargetResource(
        dt.id,
        d.id,
        d.discountCode,
        d.discountTitle,
        d.discountPercentage,
        d.discountAmount,
        d.minOrderValue,
        CAST(dt.targetType AS string),
        dt.targetId
      )
      FROM Discount d
      JOIN d.discountTargets dt
      WHERE d.isActive=true
      """)
    Page<DiscountWithTargetResource> getDiscountWithTargets(Pageable pageable);

    @Query("""
    SELECT new com.service.discount.resources.DiscountClientResource(
      d.id,
      d.discountPercentage,
      d.discountAmount,
      d.minOrderValue,
      CAST(dt.targetType AS string),
      dt.targetId
    )
    FROM Discount d
    JOIN d.discountTargets dt
    WHERE d.isActive=true AND dt.targetId = :targetId
    """)
    DiscountClientResource getByTargetIWithDiscountsClient(@Param("targetId") UUID targetId);

}
