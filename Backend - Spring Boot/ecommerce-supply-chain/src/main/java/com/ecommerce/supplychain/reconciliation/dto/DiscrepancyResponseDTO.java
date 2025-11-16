package com.ecommerce.supplychain.reconciliation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiscrepancyResponseDTO {

    private Long discrepancyId;
    private Long reportId;
    private Long productId;
    private String productName;
    private String productSku;
    private Long warehouseId;
    private Long shelfLocationId;
    private String locationCode;
    private Integer expectedQuantity;
    private Integer actualQuantity;
    private Integer varianceQuantity;
    private String varianceType;
    private Double unitCost;
    private Double varianceValue;
    private String discrepancySeverity;
    private String discrepancyCategory;
    private String rootCause;
    private String correctiveAction;
    private String assignedTo;
    private String resolutionStatus;
    private LocalDateTime resolutionDate;
    private String resolvedBy;
    private String resolutionNotes;
    private Boolean isAdjustedInSystem;
    private LocalDateTime systemAdjustmentDate;
    private String adjustedBy;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Additional calculated fields
    private Boolean requiresImmediateAttention;
    private Double variancePercentage;
    private String statusColor;
    private String impactLevel;
}