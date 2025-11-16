package com.ecommerce.supplychain.returns.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "restock_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestockRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "restock_id")
    private Long restockId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "return_order_id", nullable = false)
    private ReturnOrder returnOrder;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "product_name", nullable = false, length = 255)
    private String productName;

    @Column(name = "product_sku", nullable = false, length = 50)
    private String productSku;

    @Column(name = "warehouse_id", nullable = false)
    private Long warehouseId;

    @Column(name = "shelf_location_id")
    private Long shelfLocationId;

    @Column(name = "location_code", length = 50)
    private String locationCode;

    @Column(name = "restock_quantity", nullable = false)
    private Integer restockQuantity;

    @Column(name = "restock_date", nullable = false)
    private LocalDateTime restockDate;

    @Column(name = "restocked_by", length = 100)
    private String restockedBy;

    @Column(name = "item_condition", length = 50)
    private String itemCondition; // NEW, OPEN_BOX, REFURBISHED, USED

    @Column(name = "quality_grade", length = 20)
    private String qualityGrade;

    @Column(name = "original_cost")
    private Double originalCost;

    @Column(name = "current_value")
    private Double currentValue;

    @Column(name = "value_adjustment_reason", length = 255)
    private String valueAdjustmentReason;

    @Column(name = "requires_repair")
    private Boolean requiresRepair;

    @Column(name = "repair_notes", columnDefinition = "TEXT")
    private String repairNotes;

    @Column(name = "is_sellable")
    private Boolean isSellable;

    @Column(name = "sellable_quantity")
    private Integer sellableQuantity;

    @Column(name = "restock_notes", columnDefinition = "TEXT")
    private String restockNotes;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        restockDate = LocalDateTime.now();

        if (itemCondition == null) {
            itemCondition = "NEW";
        }
        if (isSellable == null) {
            isSellable = true;
        }
        if (sellableQuantity == null) {
            sellableQuantity = restockQuantity;
        }
        if (requiresRepair == null) {
            requiresRepair = false;
        }
        if (qualityGrade == null) {
            qualityGrade = "GOOD";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();

        // Auto-update sellable status based on condition
        if ("DAMAGED".equals(qualityGrade) || "POOR".equals(qualityGrade)) {
            isSellable = false;
            sellableQuantity = 0;
        }
    }

    public void markAsRepaired(String repairNotes) {
        this.requiresRepair = false;
        this.repairNotes = repairNotes;
        this.qualityGrade = "REFURBISHED";
        this.isSellable = true;
        this.sellableQuantity = this.restockQuantity;
    }

    public void adjustValue(Double newValue, String reason) {
        this.currentValue = newValue;
        this.valueAdjustmentReason = reason;
    }

    public boolean isFullySellable() {
        return Boolean.TRUE.equals(isSellable) && sellableQuantity != null && sellableQuantity.equals(restockQuantity);
    }
}