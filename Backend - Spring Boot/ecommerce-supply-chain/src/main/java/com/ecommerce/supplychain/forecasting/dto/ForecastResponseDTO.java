package com.ecommerce.supplychain.forecasting.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Response DTO for demand forecast information.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ForecastResponseDTO {

    private Long forecastId;
    private Long productId;
    private String productName;
    private String productSku;
    private String forecastPeriod;
    private LocalDate forecastDate;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer predictedDemand;
    private BigDecimal confidenceLevel;
    private BigDecimal historicalAccuracy;
    private BigDecimal seasonalityFactor;
    private BigDecimal trendFactor;
    private BigDecimal promotionImpact;
    private Integer baseDemand;
    private Integer adjustedDemand;
    private String forecastMethod;
    private String forecastStatus;
    private Integer actualDemand;
    private Integer forecastError;
    private BigDecimal meanAbsoluteError;
    private BigDecimal accuracyPercentage;
    private String notes;
    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isAccurate; // Based on confidence and historical accuracy
    private String accuracyLevel; // HIGH, MEDIUM, LOW
}
