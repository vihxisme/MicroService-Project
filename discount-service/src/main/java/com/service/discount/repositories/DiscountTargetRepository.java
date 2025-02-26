package com.service.discount.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.service.discount.entities.DiscountTarget;
import com.service.discount.resources.DiscountWithTargetResource;

@Repository
public interface DiscountTargetRepository extends JpaRepository<DiscountTarget, Integer> {
  @Modifying
  @Query("DELETE FROM DiscountTarget dt WHERE dt.id = :id")
  int deleteByIdCustom(@Param("id") Integer id);

  Page<DiscountTarget> findAll(Pageable pageable);

  @Query("SELECT d FROM DiscountTarget d WHERE d.targetId = :targetId AND d.discount.isActive = true")
  Optional<DiscountTarget> findActiveDiscountByTargetId(@Param("targetId") UUID targetId);

}
