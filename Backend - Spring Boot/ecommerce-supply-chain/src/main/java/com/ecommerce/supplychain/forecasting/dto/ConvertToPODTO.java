package com.ecommerce.supplychain.forecasting.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for converting reorder plan to purchase order.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConvertToPODTO {

    @NotNull(message = "Plan ID is required")
    private Long planId;

    @NotNull(message = "Purchase Order ID is required")
    private Long purchaseOrderId;

    private String notes;
}
