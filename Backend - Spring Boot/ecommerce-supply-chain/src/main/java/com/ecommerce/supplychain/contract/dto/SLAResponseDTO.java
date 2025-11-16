package com.ecommerce.supplychain.contract.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Response DTO for SLA information.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SLAResponseDTO {

    private Long slaId;
    private Long contractId;
    private String metricName;
    private String metricDescription;
    private String targetValue;
    private String measurementUnit;
    private String minimumAcceptable;
    private String penaltyClause;
    private String monitoringFrequency;
    private String status;
    private Double compliancePercentage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}