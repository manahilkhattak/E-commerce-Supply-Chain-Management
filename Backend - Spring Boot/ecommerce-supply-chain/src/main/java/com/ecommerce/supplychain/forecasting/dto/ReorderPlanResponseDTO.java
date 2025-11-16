package com.ecommerce.supplychain.forecasting.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Response DTO for reorder plan information.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReorderPlanResponseDTO {

    private Long planId;
    private Long productId;
    private String productName;
    private String productSku;
    private Long forecastId;
    private Integer currentStock;
    private Integer safetyStock;
    private Integer leadTimeDays;
    private Integer dailyDemandRate;
    private Integer reorderPoint;
    private Integer economicOrderQuantity;
    private Integer recommendedOrderQuantity;
    private String orderUrgency;
    private LocalDate expectedStockoutDate;
    private LocalDate suggestedOrderDate;
    private LocalDate expectedDeliveryDate;
    private BigDecimal estimatedCost;
    private Long supplierId;
    private String supplierName;
    private String planStatus;
    private Boolean convertedToPo;
    private Long purchaseOrderId;
    private LocalDateTime conversionDate;
    private String stockoutRiskLevel;
    private BigDecimal serviceLevelTarget;
    private BigDecimal calculatedServiceLevel;
    private String notes;
    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer daysUntilStockout;
    private Boolean needsImmediateAction;
    private String actionPriority; // Based on urgency and risk
}
