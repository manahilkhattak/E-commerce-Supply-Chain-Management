package com.ecommerce.supplychain.returns.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "return_orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReturnOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "return_order_id")
    private Long returnOrderId;

    @Column(name = "return_number", unique = true, nullable = false, length = 50)
    private String returnNumber;

    @Column(name = "order_id", nullable = false)
    private Long orderId; // Links to original customer order

    @Column(name = "order_number", nullable = false, length = 50)
    private String orderNumber;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "customer_name", length = 255)
    private String customerName;

    @Column(name = "customer_email", length = 100)
    private String customerEmail;

    @Column(name = "return_reason", nullable = false, length = 100)
    private String returnReason; // DAMAGED, WRONG_ITEM, NOT_AS_DESCRIBED, SIZE_ISSUE, CHANGE_MIND

    @Column(name = "return_description", columnDefinition = "TEXT")
    private String returnDescription;

    @Column(name = "return_type", nullable = false, length = 50)
    private String returnType; // REFUND, EXCHANGE, STORE_CREDIT

    @Column(name = "return_status", nullable = false, length = 50)
    private String returnStatus; // REQUESTED, APPROVED, PICKUP_SCHEDULED, IN_TRANSIT, RECEIVED, INSPECTING, PROCESSING, COMPLETED, REJECTED, CANCELLED

    @Column(name = "request_date", nullable = false)
    private LocalDateTime requestDate;

    @Column(name = "approval_date")
    private LocalDateTime approvalDate;

    @Column(name = "approved_by", length = 100)
    private String approvedBy;

    @Column(name = "rejection_reason", columnDefinition = "TEXT")
    private String rejectionReason;

    @Column(name = "pickup_required")
    private Boolean pickupRequired;

    @Column(name = "pickup_scheduled_date")
    private LocalDateTime pickupScheduledDate;

    @Column(name = "pickup_completed_date")
    private LocalDateTime pickupCompletedDate;

    @Column(name = "carrier_for_return", length = 100)
    private String carrierForReturn;

    @Column(name = "return_tracking_number", length = 100)
    private String returnTrackingNumber;

    @Column(name = "warehouse_id")
    private Long warehouseId; // Where items will be returned

    @Column(name = "refund_amount")
    private Double refundAmount;

    @Column(name = "refund_method", length = 50)
    private String refundMethod; // ORIGINAL_PAYMENT, STORE_CREDIT, BANK_TRANSFER

    @Column(name = "refund_status", length = 50)
    private String refundStatus; // PENDING, PROCESSED, COMPLETED, FAILED

    @Column(name = "exchange_order_id")
    private Long exchangeOrderId; // Links to new order for exchange

    @Column(name = "store_credit_amount")
    private Double storeCreditAmount;

    @Column(name = "restocking_fee")
    private Double restockingFee;

    @Column(name = "shipping_cost_refund")
    private Double shippingCostRefund;

    @Column(name = "total_refund_amount")
    private Double totalRefundAmount;

    @Column(name = "quality_check_notes", columnDefinition = "TEXT")
    private String qualityCheckNotes;

    @Column(name = "quality_grade", length = 20)
    private String qualityGrade; // EXCELLENT, GOOD, FAIR, POOR, DAMAGED

    @Column(name = "is_restockable")
    private Boolean isRestockable;

    @Column(name = "customer_comments", columnDefinition = "TEXT")
    private String customerComments;

    @Column(name = "resolution_notes", columnDefinition = "TEXT")
    private String resolutionNotes;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "returnOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReturnItem> returnItems = new ArrayList<>();

    @OneToMany(mappedBy = "returnOrder", cascade = CascadeType.ALL)
    private List<RestockRecord> restockRecords = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if (returnStatus == null) {
            returnStatus = "REQUESTED";
        }
        if (pickupRequired == null) {
            pickupRequired = false;
        }
        if (requestDate == null) {
            requestDate = LocalDateTime.now();
        }
        if (isRestockable == null) {
            isRestockable = true;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();

        // Calculate total refund amount
        calculateTotalRefund();

        // Auto-update restockable flag based on quality grade
        if (qualityGrade != null) {
            isRestockable = !"DAMAGED".equals(qualityGrade) && !"POOR".equals(qualityGrade);
        }
    }

    public void approveReturn(String approvedByUser) {
        this.returnStatus = "APPROVED";
        this.approvalDate = LocalDateTime.now();
        this.approvedBy = approvedByUser;
    }

    public void rejectReturn(String reason) {
        this.returnStatus = "REJECTED";
        this.rejectionReason = reason;
    }

    public void markAsReceived() {
        this.returnStatus = "RECEIVED";
    }

    public void completeReturn() {
        this.returnStatus = "COMPLETED";
    }

    private void calculateTotalRefund() {
        double total = 0.0;
        if (refundAmount != null) {
            total += refundAmount;
        }
        if (storeCreditAmount != null) {
            total += storeCreditAmount;
        }
        if (shippingCostRefund != null) {
            total += shippingCostRefund;
        }
        if (restockingFee != null) {
            total -= restockingFee; // Restocking fee reduces refund
        }
        this.totalRefundAmount = Math.max(0, total);
    }

    public boolean requiresPickup() {
        return Boolean.TRUE.equals(pickupRequired);
    }

    public boolean isRefundable() {
        return "REFUND".equals(returnType) || "EXCHANGE".equals(returnType);
    }
}

