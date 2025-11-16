package com.ecommerce.supplychain.quality.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for creating quality checks.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QualityCheckDTO {

    @NotNull(message = "Package ID is required")
    private Long packageId;

    @NotBlank(message = "Tracking number is required")
    private String trackingNumber;

    @NotNull(message = "Order ID is required")
    private Long orderId;

    @NotBlank(message = "Order number is required")
    private String orderNumber;

    @NotNull(message = "Warehouse ID is required")
    private Long warehouseId;

    @NotBlank(message = "Check type is required")
    @Pattern(regexp = "PACKAGING|CONTENT_VERIFICATION|WEIGHT|DIMENSIONS|SAFETY",
            message = "Check type must be PACKAGING, CONTENT_VERIFICATION, WEIGHT, DIMENSIONS, or SAFETY")
    private String checkType;

    @NotBlank(message = "Inspector name is required")
    private String inspectorName;

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
    private String checkNotes;
}
