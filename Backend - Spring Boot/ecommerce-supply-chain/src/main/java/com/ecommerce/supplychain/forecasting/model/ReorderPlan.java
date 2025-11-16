package com.ecommerce.supplychain.forecasting.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entity representing automated reorder plans based on demand forecasts.
 * Generates purchase recommendations to maintain optimal stock levels.
 */
@Entity
@Table(name = "reorder_plans")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReorderPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_id")
    private Long planId;

    @Column(name = "product_id", nullable = false)
    private Long productId; // Reference to Product from Process 5

    @Column(name = "product_name", nullable = false, length = 255)
    private String productName;

    @Column(name = "product_sku", nullable = false, length = 50)
    private String productSku;

    @Column(name = "forecast_id")
    private Long forecastId; // Reference to DemandForecast

    @Column(name = "current_stock", nullable = false)
    private Integer currentStock;

    @Column(name = "safety_stock", nullable = false)
    private Integer safetyStock;

    @Column(name = "lead_time_days", nullable = false)
    private Integer leadTimeDays;

    @Column(name = "daily_demand_rate", nullable = false)
    private Integer dailyDemandRate;

    @Column(name = "reorder_point", nullable = false)
    private Integer reorderPoint;

    @Column(name = "economic_order_quantity")
    private Integer economicOrderQuantity;

    @Column(name = "recommended_order_quantity", nullable = false)
    private Integer recommendedOrderQuantity;

    @Column(name = "order_urgency", length = 50)
    private String orderUrgency; // LOW, MEDIUM, HIGH, CRITICAL

    @Column(name = "expected_stockout_date")
    private LocalDate expectedStockoutDate;

    @Column(name = "suggested_order_date", nullable = false)
    private LocalDate suggestedOrderDate;

    @Column(name = "expected_delivery_date")
    private LocalDate expectedDeliveryDate;

    @Column(name = "estimated_cost", precision = 15, scale = 2)
    private BigDecimal estimatedCost;

    @Column(name = "supplier_id")
    private Long supplierId; // Reference to Supplier from Process 1

    @Column(name = "supplier_name", length = 255)
    private String supplierName;

    @Column(name = "plan_status", length = 50)
    private String planStatus; // DRAFT, APPROVED, CONVERTED_TO_PO, CANCELLED

    @Column(name = "converted_to_po")
    private Boolean convertedToPo;

    @Column(name = "purchase_order_id")
    private Long purchaseOrderId; // Reference to Purchase Order from Process 3

    @Column(name = "conversion_date")
    private LocalDateTime conversionDate;

    @Column(name = "stockout_risk_level", length = 20)
    private String stockoutRiskLevel; // LOW, MEDIUM, HIGH, CRITICAL

    @Column(name = "service_level_target", precision = 5, scale = 2)
    private BigDecimal serviceLevelTarget; // Desired service level (0.95 = 95%)

    @Column(name = "calculated_service_level", precision = 5, scale = 2)
    private BigDecimal calculatedServiceLevel;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if (planStatus == null) {
            planStatus = "DRAFT";
        }
        if (convertedToPo == null) {
            convertedToPo = false;
        }
        if (serviceLevelTarget == null) {
            serviceLevelTarget = new BigDecimal("0.95");
        }
        if (orderUrgency == null) {
            calculateOrderUrgency();
        }
        if (stockoutRiskLevel == null) {
            calculateStockoutRisk();
        }

        // Calculate expected dates if not provided
        if (suggestedOrderDate == null) {
            calculateSuggestedOrderDate();
        }
        if (expectedDeliveryDate == null && suggestedOrderDate != null) {
            expectedDeliveryDate = suggestedOrderDate.plusDays(leadTimeDays);
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();

        // Recalculate urgency and risk if relevant fields changed
        calculateOrderUrgency();
        calculateStockoutRisk();
    }

    /**
     * Calculate order urgency based on current stock and demand
     */
    private void calculateOrderUrgency() {
        if (currentStock <= 0) {
            orderUrgency = "CRITICAL";
        } else {
            int daysOfSupply = (int) Math.ceil((double) currentStock / dailyDemandRate);

            if (daysOfSupply <= leadTimeDays) {
                orderUrgency = "HIGH";
            } else if (daysOfSupply <= leadTimeDays + 7) {
                orderUrgency = "MEDIUM";
            } else {
                orderUrgency = "LOW";
            }
        }
    }

    /**
     * Calculate stockout risk level
     */
    private void calculateStockoutRisk() {
        if (currentStock <= safetyStock) {
            stockoutRiskLevel = "CRITICAL";
        } else if (currentStock <= reorderPoint) {
            stockoutRiskLevel = "HIGH";
        } else if (currentStock <= reorderPoint + dailyDemandRate * 3) {
            stockoutRiskLevel = "MEDIUM";
        } else {
            stockoutRiskLevel = "LOW";
        }
    }

    /**
     * Calculate suggested order date based on lead time and current stock
     */
    private void calculateSuggestedOrderDate() {
        if (dailyDemandRate > 0 && currentStock > 0) {
            int daysUntilReorder = (currentStock - reorderPoint) / dailyDemandRate;
            suggestedOrderDate = LocalDate.now().plusDays(Math.max(0, daysUntilReorder));
        } else {
            suggestedOrderDate = LocalDate.now();
        }
    }

    /**
     * Calculate economic order quantity (EOQ)
     */
    public void calculateEOQ(BigDecimal orderingCost, BigDecimal holdingCost, BigDecimal unitCost) {
        if (dailyDemandRate > 0 && holdingCost.doubleValue() > 0 && unitCost.doubleValue() > 0) {
            double annualDemand = dailyDemandRate * 365;
            double eoq = Math.sqrt((2 * annualDemand * orderingCost.doubleValue()) /
                    (holdingCost.doubleValue() * unitCost.doubleValue()));
            this.economicOrderQuantity = (int) Math.round(eoq);
        }
    }

    /**
     * Mark as converted to purchase order
     */
    public void markAsConvertedToPO(Long poId) {
        this.convertedToPo = true;
        this.purchaseOrderId = poId;
        this.planStatus = "CONVERTED_TO_PO";
        this.conversionDate = LocalDateTime.now();
    }
}