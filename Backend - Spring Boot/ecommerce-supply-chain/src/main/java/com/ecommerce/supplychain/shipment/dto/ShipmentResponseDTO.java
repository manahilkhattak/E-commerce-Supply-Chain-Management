package com.ecommerce.supplychain.shipment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShipmentResponseDTO {

    private Long shipmentId;
    private String trackingNumber;
    private Long orderId;
    private String orderNumber;
    private Long packageId;
    private String carrier;
    private String serviceType;
    private String shipmentStatus;
    private LocalDate shipmentDate;
    private LocalDate estimatedDeliveryDate;
    private LocalDate actualDeliveryDate;
    private LocalDate pickupDate;
    private BigDecimal shippingCost;
    private BigDecimal insuranceAmount;
    private BigDecimal packageWeightKg;
    private String packageDimensions;
    private String originAddress;
    private String destinationAddress;
    private String recipientName;
    private String recipientPhone;
    private String recipientEmail;
    private Boolean requiresSignature;
    private Boolean isInsured;
    private Boolean isFragile;
    private String specialInstructions;
    private String carrierTrackingUrl;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<DispatchScheduleResponseDTO> dispatchSchedules;
    private Boolean canBeDispatched;
    private String nextAction;
}