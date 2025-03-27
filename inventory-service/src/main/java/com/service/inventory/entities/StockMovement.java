package com.service.inventory.entities;

import java.sql.Timestamp;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.service.inventory.enums.MovementEnum;
import com.service.inventory.listeners.StockMovementListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "stock_movement")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EntityListeners(StockMovementListener.class)
public class StockMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "items_id", nullable = false)
    private InventoryItem inventoryItem;

    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(name = "orders_id", nullable = true)
    private UUID orderId;

    @Column(name = "orders_code", nullable = true, unique = true)
    private String ordersCode;

    @Column(name = "stock_movement_code", unique = true, nullable = false)
    private String stockMovementCode;

    @Column(name = "movement_quantity", nullable = false)
    private Integer movementQuantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "movement_type", columnDefinition = "ENUM('IN', 'OUT')")
    private MovementEnum movementType;

    @Enumerated(EnumType.STRING)
    @Column(name = "movement_status", columnDefinition = "ENUM('PENDING', 'COMPLETED', 'RETURNED') DEFAULT 'PENDING'")
    private MovementEnum movementStatus;

    @CreationTimestamp
    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", updatable = false)
    private Timestamp updatedAt;
}
