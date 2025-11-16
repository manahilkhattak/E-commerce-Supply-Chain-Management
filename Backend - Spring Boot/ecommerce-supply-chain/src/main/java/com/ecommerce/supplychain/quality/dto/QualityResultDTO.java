package com.ecommerce.supplychain.quality.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for submitting quality check results.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QualityResultDTO {

    @NotNull(message = "Check ID is required")
    private Long checkId;

    @NotNull(message = "Package integrity score is required")
    private Integer packageIntegrityScore;

    @NotNull(message = "Content accuracy score is required")
    private Integer contentAccuracyScore;

    @NotNull(message = "Label accuracy score is required")
    private Integer labelAccuracyScore;

    @NotNull(message = "Weight accuracy score is required")
    private Integer weightAccuracyScore;

    @NotNull(message = "Safety compliance score is required")
    private Integer safetyComplianceScore;

    @NotNull(message = "Package damaged status is required")
    private Boolean isPackageDamaged;

    @NotNull(message = "Content correct status is required")
    private Boolean isContentCorrect;

    @NotNull(message = "Weight accurate status is required")
    private Boolean isWeightAccurate;

    @NotNull(message = "Labels correct status is required")
    private Boolean areLabelsCorrect;

    @NotNull(message = "Hazardous compliant status is required")
    private Boolean isHazardousCompliant;

    private String issuesFound;
    private String correctiveActions;

    @NotBlank(message = "Inspector name is required")
    private String inspectorName;

    private String checkNotes;
}
