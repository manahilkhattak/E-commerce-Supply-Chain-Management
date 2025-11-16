package com.ecommerce.supplychain.exception.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "delivery_exceptions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryException {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exception_id")
    private Long exceptionId;

    @Column(name = "exception_number", unique = true, nullable = false, length = 50)
    private String exceptionNumber;

    @Column(name = "tracking_number", nullable = false, length = 100)
    private String trackingNumber;

    @Column(name = "shipment_id", nullable = false)
    private Long shipmentId; // Links to Process 11 (Shipment)

    @Column(name = "order_id", nullable = false)
    private Long orderId; // Links to Customer Order

    @Column(name = "package_id")
    private Long packageId; // Links to Process 9 (Package)

    @Column(name = "exception_type", nullable = false, length = 50)
    private String exceptionType; // DAMAGED, LOST, DELAYED, REFUSED, WRONG_ADDRESS, CUSTOMER_NOT_AVAILABLE

    @Column(name = "exception_severity", nullable = false, length = 20)
    private String exceptionSeverity; // LOW, MEDIUM, HIGH, CRITICAL

    @Column(name = "exception_description", nullable = false, columnDefinition = "TEXT")
    private String exceptionDescription;

    @Column(name = "exception_location", length = 255)
    private String exceptionLocation;

    @Column(name = "exception_date", nullable = false)
    private LocalDateTime exceptionDate;

    @Column(name = "reported_by", length = 100)
    private String reportedBy; // Carrier, Customer, System

    @Column(name = "carrier", length = 100)
    private String carrier;

    @Column(name = "carrier_contact", length = 255)
    private String carrierContact;

    @Column(name = "customer_contacted")
    private Boolean customerContacted;

    @Column(name = "customer_contact_date")
    private LocalDateTime customerContactDate;

    @Column(name = "customer_response", length = 255)
    private String customerResponse;

    @Column(name = "estimated_resolution_date")
    private LocalDateTime estimatedResolutionDate;

    @Column(name = "assigned_to", length = 100)
    private String assignedTo; // Support agent assigned

    @Column(name = "exception_status", nullable = false, length = 50)
    private String exceptionStatus; // OPEN, IN_PROGRESS, RESOLVED, ESCALATED, CLOSED

    @Column(name = "priority_level", length = 20)
    private String priorityLevel; // LOW, MEDIUM, HIGH, URGENT

    @Column(name = "requires_insurance_claim")
    private Boolean requiresInsuranceClaim;

    @Column(name = "insurance_claim_filed")
    private Boolean insuranceClaimFiled;

    @Column(name = "claim_reference", length = 100)
    private String claimReference;

    @Column(name = "estimated_compensation_amount")
    private Double estimatedCompensationAmount;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToOne(mappedBy = "deliveryException", cascade = CascadeType.ALL)
    private ExceptionResolution resolution;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if (exceptionStatus == null) {
            exceptionStatus = "OPEN";
        }
        if (exceptionSeverity == null) {
            exceptionSeverity = "MEDIUM";
        }
        if (priorityLevel == null) {
            priorityLevel = "MEDIUM";
        }
        if (customerContacted == null) {
            customerContacted = false;
        }
        if (requiresInsuranceClaim == null) {
            requiresInsuranceClaim = false;
        }
        if (insuranceClaimFiled == null) {
            insuranceClaimFiled = false;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void assignToAgent(String agent) {
        this.assignedTo = agent;
        this.exceptionStatus = "IN_PROGRESS";
    }

    public void markAsResolved() {
        this.exceptionStatus = "RESOLVED";
        if (this.resolution != null) {
            this.resolution.setResolutionDate(LocalDateTime.now());
        }
    }

    public void escalateException() {
        this.exceptionStatus = "ESCALATED";
        this.priorityLevel = "URGENT";
    }

    public boolean requiresImmediateAttention() {
        return "CRITICAL".equals(exceptionSeverity) || "URGENT".equals(priorityLevel);
    }
}