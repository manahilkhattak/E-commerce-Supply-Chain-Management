package com.ecommerce.supplychain.quality.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Entity representing quality control checks before shipment.
 * Integrates with Package (Process 9) and connects to Shipment (Process 11).
 */
@Entity
@Table(name = "quality_checks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QualityCheck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "check_id")
    private Long checkId;

    @Column(name = "check_number", unique = true, nullable = false, length = 50)
    private String checkNumber;

    @Column(name = "package_id", nullable = false)
    private Long packageId; // Reference to Package from Process 9

    @Column(name = "tracking_number", nullable = false, length = 100)
    private String trackingNumber;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "order_number", nullable = false, length = 50)
    private String orderNumber;

    @Column(name = "warehouse_id", nullable = false)
    private Long warehouseId;

    @Column(name = "check_type", nullable = false, length = 50)
    private String checkType; // PACKAGING, CONTENT_VERIFICATION, WEIGHT, DIMENSIONS, SAFETY

    @Column(name = "inspector_name", nullable = false, length = 100)
    private String inspectorName;

    @Column(name = "check_status", length = 50)
    private String checkStatus; // PENDING, IN_PROGRESS, PASSED, FAILED, CONDITIONAL_APPROVAL

    @Column(name = "overall_result", length = 20)
    private String overallResult; // PASS, FAIL, CONDITIONAL

    @Column(name = "score_percentage")
    private Double scorePercentage;

    @Column(name = "package_integrity_score")
    private Integer packageIntegrityScore; // 1-5 scale

    @Column(name = "content_accuracy_score")
    private Integer contentAccuracyScore; // 1-5 scale

    @Column(name = "label_accuracy_score")
    private Integer labelAccuracyScore; // 1-5 scale

    @Column(name = "weight_accuracy_score")
    private Integer weightAccuracyScore; // 1-5 scale

    @Column(name = "safety_compliance_score")
    private Integer safetyComplianceScore; // 1-5 scale

    @Column(name = "is_package_damaged")
    private Boolean isPackageDamaged;

    @Column(name = "is_content_correct")
    private Boolean isContentCorrect;

    @Column(name = "is_weight_accurate")
    private Boolean isWeightAccurate;

    @Column(name = "are_labels_correct")
    private Boolean areLabelsCorrect;

    @Column(name = "is_hazardous_compliant")
    private Boolean isHazardousCompliant;

    @Column(name = "issues_found", columnDefinition = "TEXT")
    private String issuesFound;

    @Column(name = "corrective_actions", columnDefinition = "TEXT")
    private String correctiveActions;

    @Column(name = "recheck_required")
    private Boolean recheckRequired;

    @Column(name = "recheck_notes", columnDefinition = "TEXT")
    private String recheckNotes;

    @Column(name = "approved_for_shipment")
    private Boolean approvedForShipment;

    @Column(name = "check_notes", columnDefinition = "TEXT")
    private String checkNotes;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if (checkStatus == null) {
            checkStatus = "PENDING";
        }
        if (packageIntegrityScore == null) {
            packageIntegrityScore = 5;
        }
        if (contentAccuracyScore == null) {
            contentAccuracyScore = 5;
        }
        if (labelAccuracyScore == null) {
            labelAccuracyScore = 5;
        }
        if (weightAccuracyScore == null) {
            weightAccuracyScore = 5;
        }
        if (safetyComplianceScore == null) {
            safetyComplianceScore = 5;
        }
        if (isPackageDamaged == null) {
            isPackageDamaged = false;
        }
        if (isContentCorrect == null) {
            isContentCorrect = true;
        }
        if (isWeightAccurate == null) {
            isWeightAccurate = true;
        }
        if (areLabelsCorrect == null) {
            areLabelsCorrect = true;
        }
        if (isHazardousCompliant == null) {
            isHazardousCompliant = true;
        }
        if (recheckRequired == null) {
            recheckRequired = false;
        }
        if (approvedForShipment == null) {
            approvedForShipment = false;
        }

        calculateOverallScore();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        calculateOverallScore();
        determineOverallResult();
    }

    /**
     * Calculate overall score percentage
     */
    public void calculateOverallScore() {
        int totalScore = packageIntegrityScore + contentAccuracyScore +
                labelAccuracyScore + weightAccuracyScore + safetyComplianceScore;
        this.scorePercentage = (totalScore / 25.0) * 100;
    }

    /**
     * Determine overall result based on scores and checks
     */
    public void determineOverallResult() {
        if (scorePercentage >= 90.0 &&
                !isPackageDamaged &&
                isContentCorrect &&
                isWeightAccurate &&
                areLabelsCorrect &&
                isHazardousCompliant) {
            this.overallResult = "PASS";
            this.approvedForShipment = true;
            this.checkStatus = "PASSED";
        } else if (scorePercentage >= 70.0) {
            this.overallResult = "CONDITIONAL";
            this.approvedForShipment = true;
            this.checkStatus = "CONDITIONAL_APPROVAL";
        } else {
            this.overallResult = "FAIL";
            this.approvedForShipment = false;
            this.checkStatus = "FAILED";
        }

        // Auto-complete if result determined
        if (overallResult != null && completedAt == null) {
            completedAt = LocalDateTime.now();
        }
    }

    /**
     * Start quality check
     */
    public void startCheck(String inspector) {
        this.inspectorName = inspector;
        this.checkStatus = "IN_PROGRESS";
        this.startedAt = LocalDateTime.now();
    }

    /**
     * Add quality issue
     */
    public void addIssue(String issue, String correctiveAction) {
        String existingIssues = this.issuesFound != null ? this.issuesFound + "\n" : "";
        this.issuesFound = existingIssues + "• " + issue;

        String existingActions = this.correctiveActions != null ? this.correctiveActions + "\n" : "";
        this.correctiveActions = existingActions + "• " + correctiveAction;

        this.recheckRequired = true;
    }

    /**
     * Mark for recheck
     */
    public void markForRecheck(String notes) {
        this.recheckRequired = true;
        this.recheckNotes = notes;
        this.checkStatus = "PENDING";
        this.approvedForShipment = false;
    }
}