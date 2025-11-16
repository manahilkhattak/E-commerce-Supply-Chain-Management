package com.ecommerce.supplychain.inventory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for stock alert management.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockAlertDTO {

    @NotNull(message = "Alert ID is required")
    private Long alertId;

    @NotBlank(message = "Resolution notes are required")
    private String resolutionNotes;

    @NotBlank(message = "Resolved by is required")
    private String resolvedBy;
}
