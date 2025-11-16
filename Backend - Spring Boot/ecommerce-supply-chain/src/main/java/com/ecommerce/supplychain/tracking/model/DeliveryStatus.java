package com.ecommerce.supplychain.tracking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "delivery_status")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_status_id")
    private Long deliveryStatusId;

    @Column(name = "tracking_number", nullable = false, unique = true, length = 100)
    private String trackingNumber;

    @Column(name = "shipment_id", nullable = false)
    private Long shipmentId;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "package_id")
    private Long packageId;

    @Column(name = "current_status", nullable = false, length = 50)
    private String currentStatus; // PENDING, SHIPPED, IN_TRANSIT, OUT_FOR_DELIVERY, DELIVERED, EXCEPTION, RETURNED

    @Column(name = "status_description", length = 255)
    private String statusDescription;

    @Column(name = "last_updated", nullable = false)
    private LocalDateTime lastUpdated;

    @Column(name = "last_location", length = 255)
    private String lastLocation;

    @Column(name = "carrier", length = 100)
    private String carrier;

    @Column(name = "service_type", length = 50)
    private String serviceType; // STANDARD, EXPRESS, OVERNIGHT

    @Column(name = "estimated_delivery")
    private LocalDateTime estimatedDelivery;

    @Column(name = "actual_delivery")
    private LocalDateTime actualDelivery;

    @Column(name = "signed_by", length = 100)
    private String signedBy;

    @Column(name = "delivery_attempts")
    private Integer deliveryAttempts;

    @Column(name = "is_delivered")
    private Boolean isDelivered;

    @Column(name = "is_exception")
    private Boolean isException;

    @Column(name = "exception_reason", length = 255)
    private String exceptionReason;

    @Column(name = "exception_resolution", length = 255)
    private String exceptionResolution;

    @Column(name = "customer_notified")
    private Boolean customerNotified;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        lastUpdated = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if (currentStatus == null) {
            currentStatus = "PENDING";
        }
        if (deliveryAttempts == null) {
            deliveryAttempts = 0;
        }
        if (isDelivered == null) {
            isDelivered = false;
        }
        if (isException == null) {
            isException = false;
        }
        if (customerNotified == null) {
            customerNotified = false;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        lastUpdated = LocalDateTime.now();

        // Auto-update delivered flag
        if ("DELIVERED".equals(currentStatus)) {
            isDelivered = true;
            if (actualDelivery == null) {
                actualDelivery = LocalDateTime.now();
            }
        }

        // Auto-update exception flag
        if ("EXCEPTION".equals(currentStatus)) {
            isException = true;
        }
    }

    public void incrementDeliveryAttempts() {
        if (deliveryAttempts == null) {
            deliveryAttempts = 0;
        }
        deliveryAttempts++;
    }

    public void markAsDelivered(String signedByName) {
        this.currentStatus = "DELIVERED";
        this.isDelivered = true;
        this.actualDelivery = LocalDateTime.now();
        this.signedBy = signedByName;
        this.deliveryAttempts = 1; // Reset attempts
    }
}