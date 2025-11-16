package com.ecommerce.supplychain.exception.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryExceptionDTO {

    @NotBlank(message = "Tracking number is required")
    private String trackingNumber;

    @NotNull(message = "Shipment ID is required")
    private Long shipmentId;

    @NotNull(message = "Order ID is required")
    private Long orderId;

    private Long packageId;

    @NotBlank(message = "Exception type is required")
    @Pattern(regexp = "DAMAGED|LOST|DELAYED|REFUSED|WRONG_ADDRESS|CUSTOMER_NOT_AVAILABLE",
            message = "Exception type must be DAMAGED, LOST, DELAYED, REFUSED, WRONG_ADDRESS, or CUSTOMER_NOT_AVAILABLE")
    private String exceptionType;

    @NotBlank(message = "Exception description is required")
    private String exceptionDescription;

    @NotBlank(message = "Exception severity is required")
    @Pattern(regexp = "LOW|MEDIUM|HIGH|CRITICAL",
            message = "Exception severity must be LOW, MEDIUM, HIGH, or CRITICAL")
    private String exceptionSeverity;

    private String exceptionLocation;

    private LocalDateTime exceptionDate;

    private String reportedBy;

    private String carrier;

    private String carrierContact;

    private String assignedTo;

    private String priorityLevel;

    private Boolean requiresInsuranceClaim;

    private Double estimatedCompensationAmount;

    private Boolean customerContacted;

    private LocalDateTime estimatedResolutionDate;
}