package com.service.inventory.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.service.inventory.entities.Inventory;

import feign.Param;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, UUID> {

    @Modifying
    @Query("UPDATE Inventory i SET i.quantity = :quantity WHERE i.id = :inventoryId")
    void updateQuantity(@Param("inventoryId") UUID inventoryId, @Param("quantity") Integer quantity);

    Page<Inventory> findAll(Pageable pageble);
}
