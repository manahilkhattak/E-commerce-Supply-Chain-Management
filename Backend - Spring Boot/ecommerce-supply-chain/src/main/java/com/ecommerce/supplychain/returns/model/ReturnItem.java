package com.ecommerce.supplychain.returns.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "return_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReturnItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "return_item_id")
    private Long returnItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "return_order_id", nullable = false)
    private ReturnOrder returnOrder;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "product_name", nullable = false, length = 255)
    private String productName;

    @Column(name = "product_sku", nullable = false, length = 50)
    private String productSku;

    @Column(name = "original_order_item_id", nullable = false)
    private Long originalOrderItemId;

    @Column(name = "return_quantity", nullable = false)
    private Integer returnQuantity;

    @Column(name = "original_quantity", nullable = false)
    private Integer originalQuantity;

    @Column(name = "unit_price", nullable = false)
    private Double unitPrice;

    @Column(name = "return_reason", length = 100)
    private String returnReason;

    @Column(name = "item_condition", length = 50)
    private String itemCondition; // NEW, LIKE_NEW, USED, DAMAGED

    @Column(name = "is_restockable")
    private Boolean isRestockable;

    @Column(name = "restock_quantity")
    private Integer restockQuantity;

    @Column(name = "quality_notes", columnDefinition = "TEXT")
    private String qualityNotes;

    @PrePersist
    protected void onCreate() {
        if (isRestockable == null) {
            isRestockable = true;
        }
        if (restockQuantity == null) {
            restockQuantity = returnQuantity;
        }
    }
}
