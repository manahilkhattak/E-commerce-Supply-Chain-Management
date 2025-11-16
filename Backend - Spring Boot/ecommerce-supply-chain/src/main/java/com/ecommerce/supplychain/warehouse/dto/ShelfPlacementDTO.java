package com.ecommerce.supplychain.warehouse.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for placing products on shelf locations.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShelfPlacementDTO {

    @NotNull(message = "Shelf ID is required")
    private Long shelfId;

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotBlank(message = "Product name is required")
    private String productName;

    @NotBlank(message = "Product SKU is required")
    private String productSku;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @NotNull(message = "Unit weight is required")
    @DecimalMin(value = "0.01", message = "Unit weight must be greater than 0")
    private Double unitWeightKg;

    @NotBlank(message = "Placed by is required")
    private String placedBy;

    private String notes;
}
