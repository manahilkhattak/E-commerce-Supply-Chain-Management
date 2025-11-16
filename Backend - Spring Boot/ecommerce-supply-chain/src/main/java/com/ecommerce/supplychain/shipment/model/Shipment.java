package com.ecommerce.supplychain.shipment.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "shipments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shipment_id")
    private Long shipmentId;

    @Column(name = "tracking_number", unique = true, nullable = false, length = 100)
    private String trackingNumber;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "order_number", nullable = false, length = 50)
    private String orderNumber;

    @Column(name = "package_id", nullable = false)
    private Long packageId;

    @Column(name = "carrier", nullable = false, length = 100)
    private String carrier;

    @Column(name = "service_type", nullable = false, length = 100)
    private String serviceType;

    @Column(name = "shipment_status", length = 50)
    private String shipmentStatus;

    @Column(name = "shipment_date")
    private LocalDate shipmentDate;

    @Column(name = "estimated_delivery_date")
    private LocalDate estimatedDeliveryDate;

    @Column(name = "actual_delivery_date")
    private LocalDate actualDeliveryDate;

    @Column(name = "pickup_date")
    private LocalDate pickupDate;

    @Column(name = "shipping_cost", precision = 10, scale = 2)
    private BigDecimal shippingCost;

    @Column(name = "insurance_amount", precision = 10, scale = 2)
    private BigDecimal insuranceAmount;

    @Column(name = "package_weight_kg", precision = 8, scale = 2)
    private BigDecimal packageWeightKg;

    @Column(name = "package_dimensions", length = 100)
    private String packageDimensions;

    @Column(name = "origin_address", columnDefinition = "TEXT")
    private String originAddress;

    @Column(name = "destination_address", columnDefinition = "TEXT", nullable = false)
    private String destinationAddress;

    @Column(name = "recipient_name", nullable = false, length = 255)
    private String recipientName;

    @Column(name = "recipient_phone", length = 20)
    private String recipientPhone;

    @Column(name = "recipient_email", length = 100)
    private String recipientEmail;

    @Column(name = "requires_signature")
    private Boolean requiresSignature;

    @Column(name = "is_insured")
    private Boolean isInsured;

    @Column(name = "is_fragile")
    private Boolean isFragile;

    @Column(name = "special_instructions", columnDefinition = "TEXT")
    private String specialInstructions;

    @Column(name = "carrier_tracking_url", length = 500)
    private String carrierTrackingUrl;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "shipment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DispatchSchedule> dispatchSchedules = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (shipmentStatus == null) {
            shipmentStatus = "SCHEDULED";
        }
        if (requiresSignature == null) {
            requiresSignature = false;
        }
        if (isInsured == null) {
            isInsured = false;
        }
        if (isFragile == null) {
            isFragile = false;
        }
        if (shipmentDate == null) {
            shipmentDate = LocalDate.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void addDispatchSchedule(DispatchSchedule schedule) {
        dispatchSchedules.add(schedule);
        schedule.setShipment(this);
    }

    public void markAsShipped() {
        this.shipmentStatus = "SHIPPED";
        this.shipmentDate = LocalDate.now();
    }

    public void markAsDelivered() {
        this.shipmentStatus = "DELIVERED";
        this.actualDeliveryDate = LocalDate.now();
    }

    public void markAsInTransit() {
        this.shipmentStatus = "IN_TRANSIT";
    }
}