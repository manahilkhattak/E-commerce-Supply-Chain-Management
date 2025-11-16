package com.ecommerce.supplychain.tracking.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrackingDTO {

    @NotBlank(message = "Tracking number is required")
    private String trackingNumber;

    @NotNull(message = "Shipment ID is required")
    private Long shipmentId;

    @NotNull(message = "Order ID is required")
    private Long orderId;

    private Long packageId;

    @NotBlank(message = "Event type is required")
    @Pattern(regexp = "SHIPPED|IN_TRANSIT|OUT_FOR_DELIVERY|DELIVERED|EXCEPTION",
            message = "Event type must be SHIPPED, IN_TRANSIT, OUT_FOR_DELIVERY, DELIVERED, or EXCEPTION")
    private String eventType;

    @NotBlank(message = "Event description is required")
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
}