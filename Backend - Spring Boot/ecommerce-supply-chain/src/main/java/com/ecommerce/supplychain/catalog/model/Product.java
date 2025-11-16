package com.ecommerce.supplychain.catalog.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "product_sku", unique = true, nullable = false)
    private String productSku;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String category;

    private String brand;

    @Column(name = "supplier_id")
    private Long supplierId;

    // Removed precision and scale - let JPA handle it automatically
    @Column(name = "cost_price")
    private BigDecimal costPrice;

    @Column(name = "selling_price")
    private BigDecimal sellingPrice;

    @Column(name = "current_stock")
    private Integer currentStock = 0;

    @Column(name = "minimum_stock_level")
    private Integer minimumStockLevel = 10;

    @Column(name = "maximum_stock_level")
    private Integer maximumStockLevel = 1000;

    @Column(name = "reorder_point")
    private Integer reorderPoint = 20;

    private Double weight;

    private String dimensions;

    @Column(name = "unit_of_measurement")
    private String unitOfMeasurement = "PIECE";

    private String barcode;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "is_active")
    private Boolean isActive = true;

    private String status = "ACTIVE";

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "last_stock_update")
    private LocalDateTime lastStockUpdate;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        this.lastStockUpdate = now;
        updateStatus();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
        updateStatus();
    }

    private void updateStatus() {
        if (this.currentStock <= 0) {
            this.status = "OUT_OF_STOCK";
        } else if (this.currentStock <= this.reorderPoint) {
            this.status = "LOW_STOCK";
        } else if (Boolean.TRUE.equals(this.isActive)) {
            this.status = "ACTIVE";
        } else {
            this.status = "INACTIVE";
        }
    }
}