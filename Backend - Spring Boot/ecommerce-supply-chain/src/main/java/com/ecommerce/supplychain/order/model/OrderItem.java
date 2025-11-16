package com.ecommerce.supplychain.order.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long orderItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "product_id", nullable = false)
    private Long productId; // Links to Process 5 (Catalog)

    @Column(name = "product_name", nullable = false, length = 255)
    private String productName;

    @Column(name = "product_sku", nullable = false, length = 50)
    private String productSku;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "total_price", precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @Column(name = "weight_kg", precision = 8, scale = 3)
    private BigDecimal weightKg;

    @Column(name = "is_fragile")
    private Boolean isFragile;

    @Column(name = "requires_quality_check")
    private Boolean requiresQualityCheck;

    @Column(name = "category", length = 100)
    private String category;

    @Column(name = "brand", length = 100)
    private String brand;

    @Column(name = "item_notes", columnDefinition = "TEXT")
    private String itemNotes;

    // Links to inventory and warehouse processes
    @Column(name = "inventory_id")
    private Long inventoryId; // Links to Process 6 (Inventory)

    @Column(name = "shelf_location_id")
    private Long shelfLocationId; // Links to Process 8 (Warehouse)

    @PrePersist
    @PreUpdate
    protected void calculateTotalPrice() {
        if (unitPrice != null && quantity != null) {
            this.totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity));
        }

        if (isFragile == null) {
            isFragile = false;
        }
        if (requiresQualityCheck == null) {
            requiresQualityCheck = false;
        }
    }
}