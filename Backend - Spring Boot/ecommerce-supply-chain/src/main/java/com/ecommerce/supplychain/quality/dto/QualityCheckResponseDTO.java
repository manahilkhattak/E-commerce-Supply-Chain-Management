package com.ecommerce.supplychain.quality.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Response DTO for quality check information.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QualityCheckResponseDTO {

    private Long checkId;
    private String checkNumber;
    private Long packageId;
    private String trackingNumber;
    private Long orderId;
    private String orderNumber;
    private Long warehouseId;
    private String checkType;
    private String inspectorName;
    private String checkStatus;
    private String overallResult;
    private Double scorePercentage;
    private Integer packageIntegrityScore;
    private Integer contentAccuracyScore;
    private Integer labelAccuracyScore;
    private Integer weightAccuracyScore;
    private Integer safetyComplianceScore;
    private Boolean isPackageDamaged;
    private Boolean isContentCorrect;
    private Boolean isWeightAccurate;
    private Boolean areLabelsCorrect;
    private Boolean isHazardousCompliant;
    private String issuesFound;
    private String correctiveActions;
    private Boolean recheckRequired;
    private String recheckNotes;
    private Boolean approvedForShipment;
    private String checkNotes;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean meetsShippingStandards;
    private String recommendation; // APPROVE, REJECT, RE_CHECK
}
