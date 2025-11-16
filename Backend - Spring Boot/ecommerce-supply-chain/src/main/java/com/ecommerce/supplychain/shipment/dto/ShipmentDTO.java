package com.ecommerce.supplychain.shipment.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentDTO {

    @NotBlank(message = "Tracking number is required")
    private String trackingNumber;

    @NotNull(message = "Order ID is required")
    private Long orderId;

    @NotBlank(message = "Order number is required")
    private String orderNumber;

    @NotNull(message = "Package ID is required")
    private Long packageId;

    @NotBlank(message = "Carrier is required")
    private String carrier;

    @NotBlank(message = "Service type is required")
    private String serviceType;

    private LocalDate shipmentDate;
    private LocalDate estimatedDeliveryDate;

    @NotNull(message = "Shipping cost is required")
    @DecimalMin(value = "0.0", message = "Shipping cost must be positive")
    private BigDecimal shippingCost;

    private BigDecimal insuranceAmount;
    private BigDecimal packageWeightKg;
    private String packageDimensions;

    @NotBlank(message = "Destination address is required")
    private String destinationAddress;

    @NotBlank(message = "Recipient name is required")
    private String recipientName;

    private String recipientPhone;
    private String recipientEmail;
    private Boolean requiresSignature;
    private Boolean isInsured;
    private Boolean isFragile;
    private String specialInstructions;
    private String notes;
}