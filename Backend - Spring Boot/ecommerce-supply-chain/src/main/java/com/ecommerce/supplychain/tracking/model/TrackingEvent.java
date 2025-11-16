package com.ecommerce.supplychain.tracking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "tracking_events")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrackingEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tracking_event_id")
    private Long trackingEventId;

    @Column(name = "tracking_number", nullable = false, length = 100)
    private String trackingNumber;

    @Column(name = "shipment_id", nullable = false)
    private Long shipmentId; // Links to Process 11 (Shipment)

    @Column(name = "order_id", nullable = false)
    private Long orderId; // Links to Customer Order

    @Column(name = "package_id")
    private Long packageId; // Links to Process 9 (Package)

    @Column(name = "event_type", nullable = false, length = 50)
    private String eventType; // SHIPPED, IN_TRANSIT, OUT_FOR_DELIVERY, DELIVERED, EXCEPTION

    @Column(name = "event_description", nullable = false, length = 255)
    private String eventDescription;

    @Column(name = "event_location", length = 255)
    private String eventLocation;

    @Column(name = "event_timestamp", nullable = false)
    private LocalDateTime eventTimestamp;

    @Column(name = "carrier", length = 100)
    private String carrier; // UPS, FedEx, DHL, etc.

    @Column(name = "carrier_status_code", length = 50)
    private String carrierStatusCode;

    @Column(name = "carrier_status_description", length = 255)
    private String carrierStatusDescription;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "estimated_delivery")
    private LocalDateTime estimatedDelivery;

    @Column(name = "signed_by", length = 100)
    private String signedBy;

    @Column(name = "delivery_notes", columnDefinition = "TEXT")
    private String deliveryNotes;

    @Column(name = "is_milestone")
    private Boolean isMilestone; // Major events like shipped, delivered

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (isMilestone == null) {
            isMilestone = false;
        }
    }
}