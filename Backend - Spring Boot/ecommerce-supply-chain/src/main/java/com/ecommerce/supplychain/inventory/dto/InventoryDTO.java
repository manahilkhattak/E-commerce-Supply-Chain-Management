package com.ecommerce.supplychain.inventory.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for inventory monitoring setup and updates.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryDTO {

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotBlank(message = "Product name is required")
    private String productName;

    @NotBlank(message = "Product SKU is required")
    private String productSku;

    @NotNull(message = "Current stock is required")
    @Min(value = 0, message = "Current stock cannot be negative")
    private Integer currentStock;

    @NotNull(message = "Minimum stock level is required")
    @Min(value = 0, message = "Minimum stock level cannot be negative")
    private Integer minimumStockLevel;

    @Min(value = 1, message = "Maximum stock level must be at least 1")
    private Integer maximumStockLevel;

    @NotNull(message = "Reorder point is required")
    @Min(value = 0, message = "Reorder point cannot be negative")
    private Integer reorderPoint;

    private Double stockValue;
    private Double stockTurnoverRate;
    private Integer daysOfSupply;
    private Boolean isMonitored;
}
