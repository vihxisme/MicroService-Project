package com.service.inventory.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.service.inventory.entities.StockMovement;
import com.service.inventory.enums.MovementEnum;
import com.service.inventory.resources.StockMovementResource;

import feign.Param;

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
        WHERE sm.movementType = 'IN' AND i.id = :inventoryId
    """)
    Page<StockMovementResource> findTypeINByInventoryID(Pageable pageable, @Param("inventoryId") UUID inventoryId);

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
            WHERE sm.movementType = 'OUT' AND i.id = :inventoryId
    """)
    Page<StockMovementResource> findTypeOUTByInventoryId(Pageable pageable, @Param("inventoryId") UUID inventoryId);

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
        WHERE i.id = :inventoryId
    """)
    Page<StockMovementResource> findByInventoryID(Pageable pageable, @Param("inventoryId") UUID inventoryId);

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
        WHERE sm.movementType = :type AND i.id = :inventoryId
    """)
    Page<StockMovementResource> findTypeByInventoryID(Pageable pageable, @Param("inventoryId") UUID inventoryId, @Param("type") MovementEnum type);

    boolean existsByStockMovementCode(String code);
}
