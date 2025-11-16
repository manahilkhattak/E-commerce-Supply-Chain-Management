package com.ecommerce.supplychain.exception.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "exception_resolutions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionResolution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resolution_id")
    private Long resolutionId;

    @OneToOne
    @JoinColumn(name = "exception_id", nullable = false)
    private DeliveryException deliveryException;

    @Column(name = "resolution_type", nullable = false, length = 50)
    private String resolutionType; // RESHIP, REFUND, COMPENSATION, EXCHANGE, DELIVERY_RETRY, CANCELLATION

    @Column(name = "resolution_description", nullable = false, columnDefinition = "TEXT")
    private String resolutionDescription;

    @Column(name = "action_taken", columnDefinition = "TEXT")
    private String actionTaken;

    @Column(name = "resolution_date")
    private LocalDateTime resolutionDate;

    @Column(name = "resolved_by", length = 100)
    private String resolvedBy;

    @Column(name = "customer_satisfaction_rating")
    private Integer customerSatisfactionRating; // 1-5 scale

    @Column(name = "compensation_amount")
    private Double compensationAmount;

    @Column(name = "compensation_approved_by", length = 100)
    private String compensationApprovedBy;

    @Column(name = "reshipment_tracking_number", length = 100)
    private String reshipmentTrackingNumber;

    @Column(name = "reshipment_date")
    private LocalDateTime reshipmentDate;

    @Column(name = "additional_notes", columnDefinition = "TEXT")
    private String additionalNotes;

    @Column(name = "resolution_duration_hours")
    private Integer resolutionDurationHours;

    @Column(name = "cost_incurred")
    private Double costIncurred;

    @Column(name = "root_cause_analysis", columnDefinition = "TEXT")
    private String rootCauseAnalysis;

    @Column(name = "preventive_measures", columnDefinition = "TEXT")
    private String preventiveMeasures;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void calculateResolutionDuration() {
        if (deliveryException != null && resolutionDate != null && deliveryException.getExceptionDate() != null) {
            long hours = java.time.Duration.between(deliveryException.getExceptionDate(), resolutionDate).toHours();
            this.resolutionDurationHours = (int) hours;
        }
    }

    public boolean isCompensationRequired() {
        return compensationAmount != null && compensationAmount > 0;
    }
}