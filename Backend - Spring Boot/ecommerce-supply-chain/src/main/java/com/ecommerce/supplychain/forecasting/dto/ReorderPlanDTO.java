package com.ecommerce.supplychain.forecasting.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

/**
 * Data Transfer Object for creating reorder plans.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReorderPlanDTO {

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotNull(message = "Forecast ID is required")
    private Long forecastId;

    @NotNull(message = "Current stock is required")
    @Min(value = 0, message = "Current stock cannot be negative")
    private Integer currentStock;

    @NotNull(message = "Safety stock is required")
    @Min(value = 0, message = "Safety stock cannot be negative")
    private Integer safetyStock;

    @NotNull(message = "Lead time days is required")
    @Min(value = 1, message = "Lead time must be at least 1 day")
    private Integer leadTimeDays;

    @NotNull(message = "Daily demand rate is required")
    @Min(value = 0, message = "Daily demand rate cannot be negative")
    private Integer dailyDemandRate;

    @NotNull(message = "Reorder point is required")
    @Min(value = 0, message = "Reorder point cannot be negative")
    private Integer reorderPoint;

    private BigDecimal estimatedCost;

    private Long supplierId;
    private String supplierName;

    @DecimalMin(value = "0.00", message = "Service level target cannot be negative")
    @DecimalMax(value = "1.00", message = "Service level target cannot exceed 1.00")
    private BigDecimal serviceLevelTarget;

    private String notes;

    @NotBlank(message = "Created by is required")
    private String createdBy;
}
