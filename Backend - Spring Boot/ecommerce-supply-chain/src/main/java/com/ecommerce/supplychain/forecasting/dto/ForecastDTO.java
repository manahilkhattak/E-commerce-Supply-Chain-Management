package com.ecommerce.supplychain.forecasting.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Data Transfer Object for creating demand forecasts.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForecastDTO {

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotBlank(message = "Product name is required")
    private String productName;

    @NotBlank(message = "Product SKU is required")
    private String productSku;

    @NotBlank(message = "Forecast period is required")
    @Pattern(regexp = "WEEKLY|MONTHLY|QUARTERLY",
            message = "Forecast period must be WEEKLY, MONTHLY, or QUARTERLY")
    private String forecastPeriod;

    @NotNull(message = "Start date is required")
    @FutureOrPresent(message = "Start date must be today or in the future")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @Future(message = "End date must be in the future")
    private LocalDate endDate;

    @NotNull(message = "Base demand is required")
    @Min(value = 0, message = "Base demand cannot be negative")
    private Integer baseDemand;

    @DecimalMin(value = "0.00", message = "Confidence level cannot be negative")
    @DecimalMax(value = "1.00", message = "Confidence level cannot exceed 1.00")
    private BigDecimal confidenceLevel;

    @DecimalMin(value = "0.00", message = "Seasonality factor cannot be negative")
    private BigDecimal seasonalityFactor;

    @DecimalMin(value = "0.00", message = "Trend factor cannot be negative")
    private BigDecimal trendFactor;

    @DecimalMin(value = "0.00", message = "Promotion impact cannot be negative")
    private BigDecimal promotionImpact;

    @NotBlank(message = "Forecast method is required")
    @Pattern(regexp = "MOVING_AVERAGE|EXPONENTIAL_SMOOTHING|ARIMA|MACHINE_LEARNING",
            message = "Forecast method must be MOVING_AVERAGE, EXPONENTIAL_SMOOTHING, ARIMA, or MACHINE_LEARNING")
    private String forecastMethod;

    private String notes;

    @NotBlank(message = "Created by is required")
    private String createdBy;
}
