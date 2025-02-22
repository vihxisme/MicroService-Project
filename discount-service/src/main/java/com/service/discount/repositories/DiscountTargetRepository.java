package com.service.discount.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.service.discount.entities.DiscountTarget;

@Repository
public interface DiscountTargetRepository extends JpaRepository<DiscountTarget, Long> {
  @Modifying
  @Query("DELETE FROM DiscountTarget dt WHERE dt.id = :id")
  int deleteByIdCustom(@Param("id") Long id);

  Page<DiscountTarget> findAll(Pageable pageable);
}
