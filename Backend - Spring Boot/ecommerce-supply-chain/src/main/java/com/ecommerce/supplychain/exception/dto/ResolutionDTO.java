package com.ecommerce.supplychain.exception.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResolutionDTO {

    @NotNull(message = "Exception ID is required")
    private Long exceptionId;

    @NotBlank(message = "Resolution type is required")
    @Pattern(regexp = "RESHIP|REFUND|COMPENSATION|EXCHANGE|DELIVERY_RETRY|CANCELLATION",
            message = "Resolution type must be RESHIP, REFUND, COMPENSATION, EXCHANGE, DELIVERY_RETRY, or CANCELLATION")
    private String resolutionType;

    @NotBlank(message = "Resolution description is required")
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

    private Double costIncurred;

    private String rootCauseAnalysis;

    private String preventiveMeasures;
}