package com.ecommerce.supplychain.reconciliation.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reconciliation_reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReconciliationReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long reportId;

    @Column(name = "report_number", unique = true, nullable = false, length = 50)
    private String reportNumber;

    @Column(name = "report_type", nullable = false, length = 50)
    private String reportType; // CYCLE_COUNT, PHYSICAL_INVENTORY, SYSTEM_AUDIT, DISCREPANCY_ANALYSIS

    @Column(name = "warehouse_id", nullable = false)
    private Long warehouseId;

    @Column(name = "warehouse_name", length = 255)
    private String warehouseName;

    @Column(name = "report_period_start", nullable = false)
    private LocalDateTime reportPeriodStart;

    @Column(name = "report_period_end", nullable = false)
    private LocalDateTime reportPeriodEnd;

    @Column(name = "conducted_by", length = 100)
    private String conductedBy;

    @Column(name = "conducted_date", nullable = false)
    private LocalDateTime conductedDate;

    @Column(name = "total_products_counted")
    private Integer totalProductsCounted;

    @Column(name = "total_sku_counted")
    private Integer totalSkuCounted;

    @Column(name = "total_expected_quantity")
    private Integer totalExpectedQuantity;

    @Column(name = "total_actual_quantity")
    private Integer totalActualQuantity;

    @Column(name = "total_discrepancies_found")
    private Integer totalDiscrepanciesFound;

    @Column(name = "discrepancy_value")
    private Double discrepancyValue;

    @Column(name = "accuracy_rate")
    private Double accuracyRate;

    @Column(name = "variance_rate")
    private Double varianceRate;

    @Column(name = "report_status", nullable = false, length = 50)
    private String reportStatus; // IN_PROGRESS, COMPLETED, UNDER_REVIEW, APPROVED

    @Column(name = "reviewed_by", length = 100)
    private String reviewedBy;

    @Column(name = "reviewed_date")
    private LocalDateTime reviewedDate;

    @Column(name = "approval_status", length = 50)
    private String approvalStatus; // PENDING, APPROVED, REJECTED

    @Column(name = "approved_by", length = 100)
    private String approvedBy;

    @Column(name = "approved_date")
    private LocalDateTime approvedDate;

    @Column(name = "summary_findings", columnDefinition = "TEXT")
    private String summaryFindings;

    @Column(name = "corrective_actions", columnDefinition = "TEXT")
    private String correctiveActions;

    @Column(name = "preventive_measures", columnDefinition = "TEXT")
    private String preventiveMeasures;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "reconciliationReport", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InventoryDiscrepancy> discrepancies = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        conductedDate = LocalDateTime.now();

        if (reportStatus == null) {
            reportStatus = "IN_PROGRESS";
        }
        if (approvalStatus == null) {
            approvalStatus = "PENDING";
        }
        if (totalProductsCounted == null) {
            totalProductsCounted = 0;
        }
        if (totalSkuCounted == null) {
            totalSkuCounted = 0;
        }
        if (totalExpectedQuantity == null) {
            totalExpectedQuantity = 0;
        }
        if (totalActualQuantity == null) {
            totalActualQuantity = 0;
        }
        if (totalDiscrepanciesFound == null) {
            totalDiscrepanciesFound = 0;
        }
        if (discrepancyValue == null) {
            discrepancyValue = 0.0;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();

        // Calculate accuracy and variance rates
        calculateMetrics();
    }

    public void addDiscrepancy(InventoryDiscrepancy discrepancy) {
        discrepancies.add(discrepancy);
        discrepancy.setReconciliationReport(this);

        // Update report totals
        totalDiscrepanciesFound = discrepancies.size();
        totalProductsCounted += 1;
        totalSkuCounted = (int) discrepancies.stream().map(InventoryDiscrepancy::getProductId).distinct().count();
        totalExpectedQuantity += discrepancy.getExpectedQuantity();
        totalActualQuantity += discrepancy.getActualQuantity();
        discrepancyValue += discrepancy.getVarianceValue();

        calculateMetrics();
    }

    public void completeReport() {
        this.reportStatus = "COMPLETED";
        calculateMetrics();
    }

    public void approveReport(String approvedByUser) {
        this.reportStatus = "APPROVED";
        this.approvalStatus = "APPROVED";
        this.approvedBy = approvedByUser;
        this.approvedDate = LocalDateTime.now();
    }

    public void markUnderReview(String reviewedByUser) {
        this.reportStatus = "UNDER_REVIEW";
        this.reviewedBy = reviewedByUser;
        this.reviewedDate = LocalDateTime.now();
    }

    private void calculateMetrics() {
        if (totalExpectedQuantity > 0) {
            this.accuracyRate = ((double) (totalExpectedQuantity - Math.abs(totalExpectedQuantity - totalActualQuantity)) / totalExpectedQuantity) * 100;
            this.varianceRate = (Math.abs((double) (totalExpectedQuantity - totalActualQuantity) / totalExpectedQuantity)) * 100;
        } else {
            this.accuracyRate = 100.0;
            this.varianceRate = 0.0;
        }
    }

    public boolean isHighVariance() {
        return varianceRate != null && varianceRate > 5.0; // More than 5% variance
    }

    public String getAccuracyGrade() {
        if (accuracyRate == null) return "N/A";
        if (accuracyRate >= 99.5) return "A+";
        if (accuracyRate >= 99.0) return "A";
        if (accuracyRate >= 98.0) return "B";
        if (accuracyRate >= 95.0) return "C";
        return "D";
    }
}