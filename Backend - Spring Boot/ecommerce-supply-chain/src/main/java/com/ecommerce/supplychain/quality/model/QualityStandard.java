package com.ecommerce.supplychain.quality.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Entity representing quality standards and thresholds for different check types.
 * Defines pass/fail criteria for quality control.
 */
@Entity
@Table(name = "quality_standards")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QualityStandard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "standard_id")
    private Long standardId;

    @Column(name = "standard_code", unique = true, nullable = false, length = 50)
    private String standardCode;

    @Column(name = "standard_name", nullable = false, length = 255)
    private String standardName;

    @Column(name = "check_type", nullable = false, length = 50)
    private String checkType; // PACKAGING, CONTENT_VERIFICATION, WEIGHT, DIMENSIONS, SAFETY

    @Column(name = "product_category", length = 100)
    private String productCategory; // ELECTRONICS, CLOTHING, FOOD, etc.

    @Column(name = "minimum_score", nullable = false)
    private Integer minimumScore; // 1-5 scale

    @Column(name = "target_score", nullable = false)
    private Integer targetScore; // 1-5 scale

    @Column(name = "weight_tolerance_percentage")
    private Double weightTolerancePercentage;

    @Column(name = "dimension_tolerance_percentage")
    private Double dimensionTolerancePercentage;

    @Column(name = "required_documentation", columnDefinition = "TEXT")
    private String requiredDocumentation;

    @Column(name = "safety_requirements", columnDefinition = "TEXT")
    private String safetyRequirements;

    @Column(name = "packaging_requirements", columnDefinition = "TEXT")
    private String packagingRequirements;

    @Column(name = "labeling_requirements", columnDefinition = "TEXT")
    private String labelingRequirements;

    @Column(name = "testing_procedures", columnDefinition = "TEXT")
    private String testingProcedures;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "version", length = 20)
    private String version;

    @Column(name = "effective_date")
    private LocalDateTime effectiveDate;

    @Column(name = "expiry_date")
    private LocalDateTime expiryDate;

    @Column(name = "created_by", length = 100)
    private String createdBy;

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

        if (minimumScore == null) {
            minimumScore = 3;
        }
        if (targetScore == null) {
            targetScore = 5;
        }
        if (weightTolerancePercentage == null) {
            weightTolerancePercentage = 5.0;
        }
        if (dimensionTolerancePercentage == null) {
            dimensionTolerancePercentage = 3.0;
        }
        if (isActive == null) {
            isActive = true;
        }
        if (version == null) {
            version = "1.0";
        }
        if (effectiveDate == null) {
            effectiveDate = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /**
     * Check if a score meets this standard
     */
    public boolean meetsStandard(Integer actualScore) {
        return actualScore != null && actualScore >= minimumScore;
    }

    /**
     * Check if weight is within tolerance
     */
    public boolean isWeightWithinTolerance(Double expectedWeight, Double actualWeight) {
        if (expectedWeight == null || actualWeight == null) return false;

        double tolerance = expectedWeight * (weightTolerancePercentage / 100);
        double difference = Math.abs(expectedWeight - actualWeight);
        return difference <= tolerance;
    }

    /**
     * Check if standard is currently effective
     */
    public boolean isCurrentlyEffective() {
        LocalDateTime now = LocalDateTime.now();
        boolean isAfterEffective = effectiveDate == null || now.isAfter(effectiveDate);
        boolean isBeforeExpiry = expiryDate == null || now.isBefore(expiryDate);
        return isActive && isAfterEffective && isBeforeExpiry;
    }
}