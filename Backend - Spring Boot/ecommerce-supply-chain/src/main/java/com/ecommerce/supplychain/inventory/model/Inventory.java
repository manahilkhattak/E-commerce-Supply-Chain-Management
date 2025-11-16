package com.ecommerce.supplychain.inventory.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Entity representing real-time inventory tracking and monitoring.
 * Integrates with Process 5 (Catalog) for stock level monitoring.
 */
@Entity
@Table(name = "inventory_monitoring")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventory_id")
    private Long inventoryId;

    @Column(name = "product_id", nullable = false, unique = true)
    private Long productId; // Reference to Product from Process 5

    @Column(name = "product_name", nullable = false, length = 255)
    private String productName;

    @Column(name = "product_sku", nullable = false, length = 50)
    private String productSku;

    @Column(name = "current_stock", nullable = false)
    private Integer currentStock;

    @Column(name = "reserved_stock")
    private Integer reservedStock; // Stock reserved for orders

    @Column(name = "available_stock", nullable = false)
    private Integer availableStock; // currentStock - reservedStock

    @Column(name = "minimum_stock_level", nullable = false)
    private Integer minimumStockLevel;

    @Column(name = "maximum_stock_level")
    private Integer maximumStockLevel;

    @Column(name = "reorder_point", nullable = false)
    private Integer reorderPoint;

    @Column(name = "stock_value")
    private Double stockValue; // currentStock * costPrice

    @Column(name = "stock_turnover_rate")
    private Double stockTurnoverRate; // Sales / Average Inventory

    @Column(name = "days_of_supply")
    private Integer daysOfSupply; // How many days current stock will last

    @Column(name = "stock_status", length = 50)
    private String stockStatus; // OPTIMAL, LOW, CRITICAL, OUT_OF_STOCK, OVERSTOCK

    @Column(name = "last_restocked_date")
    private LocalDateTime lastRestockedDate;

    @Column(name = "last_sold_date")
    private LocalDateTime lastSoldDate;

    @Column(name = "movement_frequency", length = 50)
    private String movementFrequency; // HIGH, MEDIUM, LOW, NONE

    @Column(name = "is_monitored")
    private Boolean isMonitored;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if (currentStock == null) {
            currentStock = 0;
        }
        if (reservedStock == null) {
            reservedStock = 0;
        }
        if (availableStock == null) {
            availableStock = currentStock - reservedStock;
        }
        if (minimumStockLevel == null) {
            minimumStockLevel = 10;
        }
        if (maximumStockLevel == null) {
            maximumStockLevel = 1000;
        }
        if (reorderPoint == null) {
            reorderPoint = 20;
        }
        if (isMonitored == null) {
            isMonitored = true;
        }

        updateStockStatus();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        availableStock = currentStock - reservedStock;
        updateStockStatus();
    }

    /**
     * Automatically update stock status based on current levels
     */
    private void updateStockStatus() {
        if (currentStock <= 0) {
            stockStatus = "OUT_OF_STOCK";
        } else if (currentStock <= minimumStockLevel) {
            stockStatus = "CRITICAL";
        } else if (currentStock <= reorderPoint) {
            stockStatus = "LOW";
        } else if (maximumStockLevel != null && currentStock > maximumStockLevel * 0.9) {
            stockStatus = "OVERSTOCK";
        } else {
            stockStatus = "OPTIMAL";
        }
    }

    /**
     * Reserve stock for orders
     */
    public boolean reserveStock(Integer quantity) {
        if (availableStock >= quantity) {
            this.reservedStock += quantity;
            this.availableStock = this.currentStock - this.reservedStock;
            return true;
        }
        return false;
    }

    /**
     * Release reserved stock
     */
    public void releaseReservedStock(Integer quantity) {
        this.reservedStock = Math.max(0, this.reservedStock - quantity);
        this.availableStock = this.currentStock - this.reservedStock;
    }

    /**
     * Update stock after sale
     */
    public boolean sellStock(Integer quantity) {
        if (availableStock >= quantity) {
            this.currentStock -= quantity;
            this.availableStock = this.currentStock - this.reservedStock;
            this.lastSoldDate = LocalDateTime.now();
            updateStockStatus();
            return true;
        }
        return false;
    }
}