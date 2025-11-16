package com.ecommerce.supplychain.contract.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating SLAs within a contract.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SLACreationDTO {

    @NotNull(message = "Contract ID is required")
    private Long contractId;

    @NotBlank(message = "Metric name is required")
    private String metricName;

    @NotBlank(message = "Metric description is required")
    private String metricDescription;

    @NotBlank(message = "Target value is required")
    private String targetValue;

    @NotBlank(message = "Measurement unit is required")
    @Pattern(regexp = "PERCENTAGE|HOURS|DAYS|COUNT",
            message = "Measurement unit must be PERCENTAGE, HOURS, DAYS, or COUNT")
    private String measurementUnit;

    @NotBlank(message = "Minimum acceptable value is required")
    private String minimumAcceptable;

    @NotBlank(message = "Penalty clause is required")
    private String penaltyClause;

    @NotBlank(message = "Monitoring frequency is required")
    @Pattern(regexp = "DAILY|WEEKLY|MONTHLY|QUARTERLY",
            message = "Monitoring frequency must be DAILY, WEEKLY, MONTHLY, or QUARTERLY")
    private String monitoringFrequency;
}