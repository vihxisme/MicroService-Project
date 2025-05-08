package com.service.inventory.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.service.inventory.entities.InventoryItem;

import feign.Param;

@Repository
public interface InventoryItemRepository extends JpaRepository<InventoryItem, UUID> {

    @Query("SELECT SUM(ii.itemQuantity) FROM InventoryItem ii WHERE ii.inventory.id = :inventoryId")
    Integer calculateTotalQuantity(@Param("inventoryId") UUID inventoryId);

    @Modifying
    @Query("UPDATE InventoryItem ii SET ii.itemQuantity = ii.itemQuantity + :quantity WHERE ii.id = :id")
    void inventoryIntake(@Param("id") UUID id, @Param("quantity") int quantity);

    @Query("SELECT ii FROM InventoryItem ii WHERE ii.inventory.id = :inventoryId AND ii.inventory.isAllowed = true")
    Page<InventoryItem> findAll(@Param("inventoryId") UUID inventoryId, Pageable pageable);

    @Query("SELECT SUM(i.itemQuantity) FROM InventoryItem i WHERE i.inventory.id = :inventoryId")
    Integer sumItemQuantityByInventoryId(@Param("inventoryId") UUID inventoryId);

    InventoryItem findByProdVariantId(Integer prodVariantId);

    @Query("SELECT ii FROM InventoryItem ii WHERE ii.prodVariantId IN :prodVariantIds")
    List<InventoryItem> findByProdVariantIds(@Param("prodVariantIds") List<Integer> prodVariantIds);
}
