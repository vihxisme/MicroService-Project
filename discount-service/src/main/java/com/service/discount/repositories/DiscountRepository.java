package com.service.discount.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.service.discount.entities.Discount;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, UUID> {
  @Modifying
  @Query("DELETE FROM Discount d WHERE d.id = :id")
  int deleteByIdCustom(@Param("id") UUID id);

  Page<Discount> findAll(Pageable pageable);

  Boolean existsByDiscountCode(String code);
}
