package com.ecommerce.supplychain.contract.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entity representing Service Level Agreement (SLA) terms within a contract.
 * Defines performance metrics and penalties for contract compliance.
 */
@Entity
@Table(name = "slas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SLA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sla_id")
    private Long slaId;

    @Column(name = "contract_id", nullable = false)
    private Long contractId; // Foreign key reference to Contract

    @Column(name = "metric_name", nullable = false, length = 255)
    private String metricName; // e.g., "Delivery Time", "Quality Rate", "Response Time"

    @Column(name = "metric_description", columnDefinition = "TEXT")
    private String metricDescription;

    @Column(name = "target_value", length = 100)
    private String targetValue; // e.g., "95%", "24 hours", "99.9%"

    @Column(name = "measurement_unit", length = 50)
    private String measurementUnit; // PERCENTAGE, HOURS, DAYS, COUNT

    @Column(name = "minimum_acceptable", length = 100)
    private String minimumAcceptable; // Threshold below which penalties apply

    @Column(name = "penalty_clause", columnDefinition = "TEXT")
    private String penaltyClause;

    @Column(name = "monitoring_frequency", length = 50)
    private String monitoringFrequency; // DAILY, WEEKLY, MONTHLY, QUARTERLY

    @Column(name = "status", length = 50)
    private String status; // ACTIVE, INACTIVE, VIOLATED, MET

    @Column(name = "compliance_percentage")
    private Double compliancePercentage; // Current compliance level

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) {
            status = "ACTIVE";
        }
        if (compliancePercentage == null) {
            compliancePercentage = 0.0;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}