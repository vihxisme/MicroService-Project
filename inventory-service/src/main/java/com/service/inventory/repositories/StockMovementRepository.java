package com.service.inventory.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.service.inventory.entities.StockMovement;
import com.service.inventory.resources.StockMovementResource;

@Repository
public interface StockMovementRepository extends JpaRepository<StockMovement, UUID> {

    @Query("""
            SELECT new com.service.inventory.resources.StockMovementResource(
                sm.id,
                sm.orderId,
                sm.ordersCode,
                i.productId,
                i.productCode,
                ii.prodVariantId,
                sm.stockMovementCode,
                sm.movementQuantity,
                CAST(sm.movementType AS String),
                CAST(sm.movementStatus AS String)
            )
            FROM 
                StockMovement sm
                JOIN sm.inventoryItem ii
                JOIN ii.inventory i
            WHERE sm.movementType = 'IN'
    """)
    Page<StockMovementResource> findAllTypeIN(Pageable pageable);

    @Query("""
            SELECT new com.service.inventory.resources.StockMovementResource(
                sm.id,
                sm.orderId,
                sm.ordersCode,
                i.productId,
                i.productCode,
                ii.prodVariantId,
                sm.stockMovementCode,
                sm.movementQuantity,
                CAST(sm.movementType AS String),
                CAST(sm.movementStatus AS String)
            )
            FROM 
                StockMovement sm
                JOIN sm.inventoryItem ii
                JOIN ii.inventory i
            WHERE sm.movementType = 'OUT'
    """)
    Page<StockMovementResource> findAllTypeOUT(Pageable pageable);

    boolean existsByStockMovementCode(String code);
}
