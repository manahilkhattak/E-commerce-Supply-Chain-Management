package com.ecommerce.supplychain.exception.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryExceptionResponseDTO {

    private Long exceptionId;
    private String exceptionNumber;
    private String trackingNumber;
    private Long shipmentId;
    private Long orderId;
    private Long packageId;
    private String exceptionType;
    private String exceptionSeverity;
    private String exceptionDescription;
    private String exceptionLocation;
    private LocalDateTime exceptionDate;
    private String reportedBy;
    private String carrier;
    private String carrierContact;
    private Boolean customerContacted;
    private LocalDateTime customerContactDate;
    private String customerResponse;
    private LocalDateTime estimatedResolutionDate;
    private String assignedTo;
    private String exceptionStatus;
    private String priorityLevel;
    private Boolean requiresInsuranceClaim;
    private Boolean insuranceClaimFiled;
    private String claimReference;
    private Double estimatedCompensationAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Additional calculated fields
    private Boolean requiresImmediateAttention;
    private Long daysOpen;
    private String statusColor;
    private Boolean hasResolution;
    private ResolutionResponseDTO resolution;
}