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
public class DeliveryStatusResponseDTO {

    private Long deliveryStatusId;
    private String trackingNumber;
    private Long shipmentId;
    private Long orderId;
    private Long packageId;
    private String currentStatus;
    private String statusDescription;
    private LocalDateTime lastUpdated;
    private String lastLocation;
    private String carrier;
    private String serviceType;
    private LocalDateTime estimatedDelivery;
    private LocalDateTime actualDelivery;
    private String signedBy;
    private Integer deliveryAttempts;
    private Boolean isDelivered;
    private Boolean isException;
    private String exceptionReason;
    private String exceptionResolution;
    private Boolean customerNotified;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Additional calculated fields
    private String formattedLastUpdated;
    private String formattedEstimatedDelivery;
    private Long daysInTransit;
    private Boolean isDelayed;
    private String nextExpectedAction;
}