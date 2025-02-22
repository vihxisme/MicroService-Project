package com.service.discount.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.service.discount.entities.DiscountType;

@Repository
public interface DiscountTypeRepository extends JpaRepository<DiscountType, Long> {
  @Modifying
  @Query("DELETE FROM DiscountType dt WHERE dt.id = :id")
  int deleteByIdCustom(@Param("id") Long id);

  Boolean existsByTypeCode(@Param("code") String code);
}
