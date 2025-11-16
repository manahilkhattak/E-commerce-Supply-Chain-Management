package com.ecommerce.supplychain.reconciliation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReconciliationResponseDTO {

    private Long reportId;
    private String reportNumber;
    private String reportType;
    private Long warehouseId;
    private String warehouseName;
    private LocalDateTime reportPeriodStart;
    private LocalDateTime reportPeriodEnd;
    private String conductedBy;
    private LocalDateTime conductedDate;
    private Integer totalProductsCounted;
    private Integer totalSkuCounted;
    private Integer totalExpectedQuantity;
    private Integer totalActualQuantity;
    private Integer totalDiscrepanciesFound;
    private Double discrepancyValue;
    private Double accuracyRate;
    private Double varianceRate;
    private String reportStatus;
    private String reviewedBy;
    private LocalDateTime reviewedDate;
    private String approvalStatus;
    private String approvedBy;
    private LocalDateTime approvedDate;
    private String summaryFindings;
    private String correctiveActions;
    private String preventiveMeasures;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Additional calculated fields
    private Boolean isHighVariance;
    private String accuracyGrade;
    private String performanceRating;
    private List<DiscrepancyResponseDTO> discrepancies;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class DiscrepancyAnalysisDTO {
    private String severityLevel;
    private Long count;
    private Double totalValue;
    private Double percentage;
}