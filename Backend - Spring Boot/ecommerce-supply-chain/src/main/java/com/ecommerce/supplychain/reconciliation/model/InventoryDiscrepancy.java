package com.ecommerce.supplychain.reconciliation.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_discrepancies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryDiscrepancy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "discrepancy_id")
    private Long discrepancyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id", nullable = false)
    private ReconciliationReport reconciliationReport;

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

    @Column(name = "expected_quantity", nullable = false)
    private Integer expectedQuantity;

    @Column(name = "actual_quantity", nullable = false)
    private Integer actualQuantity;

    @Column(name = "variance_quantity", nullable = false)
    private Integer varianceQuantity;

    @Column(name = "variance_value", nullable = false)
    private Double varianceValue;

    @Column(name = "unit_cost", nullable = false)
    private Double unitCost;


    @Column(name = "discrepancy_severity", length = 20)
    private String discrepancySeverity; // LOW, MEDIUM, HIGH, CRITICAL

    @Column(name = "discrepancy_category", length = 50)
    private String discrepancyCategory; // COUNT_ERROR, THEFT, DAMAGE, SYSTEM_ERROR, RECEIVING_ERROR, PICKING_ERROR

    @Column(name = "root_cause", length = 255)
    private String rootCause;

    @Column(name = "corrective_action", columnDefinition = "TEXT")
    private String correctiveAction;

    @Column(name = "assigned_to", length = 100)
    private String assignedTo;

    @Column(name = "resolution_status", length = 50)
    private String resolutionStatus; // OPEN, IN_PROGRESS, RESOLVED, CLOSED

    @Column(name = "resolution_date")
    private LocalDateTime resolutionDate;

    @Column(name = "resolved_by", length = 100)
    private String resolvedBy;

    @Column(name = "resolution_notes", columnDefinition = "TEXT")
    private String resolutionNotes;

    @Column(name = "is_adjusted_in_system")
    private Boolean isAdjustedInSystem;

    @Column(name = "system_adjustment_date")
    private LocalDateTime systemAdjustmentDate;

    @Column(name = "adjusted_by", length = 100)
    private String adjustedBy;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        // Calculate variance
        calculateVariance();

        if (resolutionStatus == null) {
            resolutionStatus = "OPEN";
        }
        if (isAdjustedInSystem == null) {
            isAdjustedInSystem = false;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        calculateVariance();
    }
    @Column(name = "variance_type")
    private String varianceType;

    private void calculateVariance() {
        this.varianceQuantity = this.actualQuantity - this.expectedQuantity;


        // Determine variance type
        if (this.varianceQuantity > 0) {
            this.varianceType = "OVERAGE";
        } else if (this.varianceQuantity < 0) {
            this.varianceType = "SHORTAGE";
        } else {
            this.varianceType = "ZERO";
        }

        // Calculate variance value
        this.varianceValue = Math.abs(this.varianceQuantity) * this.unitCost;

        // Determine severity
        determineSeverity();
    }

    private void determineSeverity() {
        double valueImpact = this.varianceValue;

        if (valueImpact >= 1000) {
            this.discrepancySeverity = "CRITICAL";
        } else if (valueImpact >= 500) {
            this.discrepancySeverity = "HIGH";
        } else if (valueImpact >= 100) {
            this.discrepancySeverity = "MEDIUM";
        } else {
            this.discrepancySeverity = "LOW";
        }

        // Adjust severity for zero stock situations
        if (this.expectedQuantity > 0 && this.actualQuantity == 0) {
            this.discrepancySeverity = "HIGH";
        }
    }

    public void assignForResolution(String assignedToUser) {
        this.assignedTo = assignedToUser;
        this.resolutionStatus = "IN_PROGRESS";
    }

    public void markAsResolved(String resolvedByUser, String resolutionNotes) {
        this.resolutionStatus = "RESOLVED";
        this.resolvedBy = resolvedByUser;
        this.resolutionDate = LocalDateTime.now();
        this.resolutionNotes = resolutionNotes;
    }

    public void markAsAdjusted(String adjustedByUser) {
        this.isAdjustedInSystem = true;
        this.adjustedBy = adjustedByUser;
        this.systemAdjustmentDate = LocalDateTime.now();
    }

    public boolean requiresImmediateAttention() {
        return "CRITICAL".equals(discrepancySeverity) || "HIGH".equals(discrepancySeverity);
    }

    public double getVariancePercentage() {
        if (expectedQuantity == 0) return 0.0;
        return (Math.abs(varianceQuantity) / (double) expectedQuantity) * 100;
    }
}