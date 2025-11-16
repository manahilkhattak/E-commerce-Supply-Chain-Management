package com.ecommerce.supplychain.tracking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrackingResponseDTO {

    private Long trackingEventId;
    private String trackingNumber;
    private Long shipmentId;
    private Long orderId;
    private Long packageId;
    private String eventType;
    private String eventDescription;
    private String eventLocation;
    private LocalDateTime eventTimestamp;
    private String carrier;
    private String carrierStatusCode;
    private String carrierStatusDescription;
    private Double latitude;
    private Double longitude;
    private LocalDateTime estimatedDelivery;
    private String signedBy;
    private String deliveryNotes;
    private Boolean isMilestone;
    private LocalDateTime createdAt;

    // Additional calculated fields
    private String formattedEventTime;
    private Boolean isDelivered;
    private String statusColor; // For UI purposes
}