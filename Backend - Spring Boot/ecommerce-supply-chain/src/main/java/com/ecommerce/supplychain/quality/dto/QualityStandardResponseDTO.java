package com.ecommerce.supplychain.quality.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Response DTO for quality standard information.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QualityStandardResponseDTO {

    private Long standardId;
    private String standardCode;
    private String standardName;
    private String checkType;
    private String productCategory;
    private Integer minimumScore;
    private Integer targetScore;
    private Double weightTolerancePercentage;
    private Double dimensionTolerancePercentage;
    private String requiredDocumentation;
    private String safetyRequirements;
    private String packagingRequirements;
    private String labelingRequirements;
    private String testingProcedures;
    private Boolean isActive;
    private String version;
    private LocalDateTime effectiveDate;
    private LocalDateTime expiryDate;
    private String createdBy;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isCurrentlyEffective;
}
