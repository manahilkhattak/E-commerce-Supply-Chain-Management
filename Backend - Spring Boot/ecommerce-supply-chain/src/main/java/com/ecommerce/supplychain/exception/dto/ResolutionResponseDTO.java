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
public class ResolutionResponseDTO {

    private Long resolutionId;
    private Long exceptionId;
    private String resolutionType;
    private String resolutionDescription;
    private String actionTaken;
    private LocalDateTime resolutionDate;
    private String resolvedBy;
    private Integer customerSatisfactionRating;
    private Double compensationAmount;
    private String compensationApprovedBy;
    private String reshipmentTrackingNumber;
    private LocalDateTime reshipmentDate;
    private String additionalNotes;
    private Integer resolutionDurationHours;
    private Double costIncurred;
    private String rootCauseAnalysis;
    private String preventiveMeasures;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Additional calculated fields
    private Boolean isCompensationRequired;
    private String resolutionEfficiency;
}